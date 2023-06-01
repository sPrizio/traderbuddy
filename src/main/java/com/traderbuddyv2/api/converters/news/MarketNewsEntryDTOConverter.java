package com.traderbuddyv2.api.converters.news;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.models.dto.news.MarketNewsEntryDTO;
import com.traderbuddyv2.core.models.entities.news.MarketNewsEntry;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Converter that converts {@link MarketNewsEntry}s into {@link MarketNewsEntryDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("marketNewsEntryDTOConverter")
public class MarketNewsEntryDTOConverter implements GenericDTOConverter<MarketNewsEntry, MarketNewsEntryDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public MarketNewsEntryDTO convert(final MarketNewsEntry entity) {

        if (entity == null) {
            return new MarketNewsEntryDTO();
        }

        final MarketNewsEntryDTO marketNewsEntryDTO = new MarketNewsEntryDTO();

        marketNewsEntryDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        marketNewsEntryDTO.setContent(entity.getContent());
        marketNewsEntryDTO.setSeverity(entity.getSeverity().getDescription());
        marketNewsEntryDTO.setSeverityLevel(entity.getSeverity().getLevel());
        marketNewsEntryDTO.setCountry(entity.getCountry().getCurrency().getIsoCode());

        return marketNewsEntryDTO;
    }
}
