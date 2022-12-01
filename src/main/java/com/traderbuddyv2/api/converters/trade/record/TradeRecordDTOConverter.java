package com.traderbuddyv2.api.converters.trade.record;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.converters.account.AccountDTOConverter;
import com.traderbuddyv2.api.models.dto.trade.record.TradeRecordDTO;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Converts {@link TradeRecord}s to {@link TradeRecordDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("tradeRecordDTOConverter")
public class TradeRecordDTOConverter implements GenericDTOConverter<TradeRecord, TradeRecordDTO> {

    @Resource(name = "accountDTOConverter")
    private AccountDTOConverter accountDTOConverter;

    @Resource(name = "mathService")
    private MathService mathService;

    @Resource(name = "tradeRecordStatisticsDTOConverter")
    private TradeRecordStatisticsDTOConverter tradeRecordStatisticsDTOConverter;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public TradeRecordDTO convert(final TradeRecord entity) {

        if (entity == null) {
            return new TradeRecordDTO();
        }

        TradeRecordDTO tradeRecordDTO = new TradeRecordDTO();

        tradeRecordDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        tradeRecordDTO.setAggregateInterval(entity.getAggregateInterval());
        tradeRecordDTO.setAccount(this.accountDTOConverter.convert(entity.getAccount()));
        tradeRecordDTO.setBalance(this.mathService.getDouble(entity.getBalance()));
        tradeRecordDTO.setStatistics(this.tradeRecordStatisticsDTOConverter.convert(entity.getStatistics()));
        tradeRecordDTO.setEndDate(entity.getEndDate());
        tradeRecordDTO.setStartDate(entity.getStartDate());

        return tradeRecordDTO;
    }
}
