package com.traderbuddyv2.api.converters.retrospective;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.converters.account.AccountDTOConverter;
import com.traderbuddyv2.api.converters.trade.record.TradeRecordDTOConverter;
import com.traderbuddyv2.api.models.dto.retrospective.RetrospectiveDTO;
import com.traderbuddyv2.core.models.entities.retrospective.Retrospective;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import com.traderbuddyv2.core.services.trade.record.TradeRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Converter for {@link Retrospective}s into {@link RetrospectiveDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("retrospectiveDTOConverter")
public class RetrospectiveDTOConverter implements GenericDTOConverter<Retrospective, RetrospectiveDTO> {

    @Resource(name = "accountDTOConverter")
    private AccountDTOConverter accountDTOConverter;

    @Resource(name = "tradeRecordDTOConverter")
    private TradeRecordDTOConverter tradeRecordDTOConverter;

    @Resource(name = "tradeRecordService")
    private TradeRecordService tradeRecordService;

    @Resource(name = "retrospectiveEntryDTOConverter")
    private RetrospectiveEntryDTOConverter retrospectiveEntryDTOConverter;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public RetrospectiveDTO convert(final Retrospective entity) {

        if (entity == null) {
            return new RetrospectiveDTO();
        }

        RetrospectiveDTO retrospectiveDTO = new RetrospectiveDTO();

        retrospectiveDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        retrospectiveDTO.setStartDate(entity.getStartDate());
        retrospectiveDTO.setEndDate(entity.getEndDate());
        retrospectiveDTO.setIntervalFrequency(entity.getIntervalFrequency());
        retrospectiveDTO.setAccount(this.accountDTOConverter.convert(entity.getAccount()));
        retrospectiveDTO.setRetrospectiveType(entity.getRetrospectiveType().name());
        retrospectiveDTO.setMediaPath(entity.getMediaPath());

        if (CollectionUtils.isNotEmpty(entity.getPoints())) {
            retrospectiveDTO.setPoints(this.retrospectiveEntryDTOConverter.convertAll(entity.getPoints()));
        }

        this.tradeRecordService.findTradeRecordForStartDateAndEndDateAndInterval(
                        entity.getStartDate(),
                        entity.getEndDate(),
                        entity.getIntervalFrequency()
                )
                .ifPresent(rec -> retrospectiveDTO.setTotals(this.tradeRecordDTOConverter.convert(rec)));

        return retrospectiveDTO;
    }
}
