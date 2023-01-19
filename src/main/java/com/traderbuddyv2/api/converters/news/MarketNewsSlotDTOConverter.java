package com.traderbuddyv2.api.converters.news;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.models.dto.news.MarketNewsSlotDTO;
import com.traderbuddyv2.core.models.entities.news.MarketNewsSlot;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Converter that converts {@link MarketNewsSlot}s into {@link MarketNewsSlotDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("marketNewsSlotDTOConverter")
public class MarketNewsSlotDTOConverter implements GenericDTOConverter<MarketNewsSlot, MarketNewsSlotDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;

    @Resource(name = "marketNewsEntryDTOConverter")
    private MarketNewsEntryDTOConverter marketNewsEntryDTOConverter;


    //  METHODS

    @Override
    public MarketNewsSlotDTO convert(final MarketNewsSlot entity) {

        if (entity == null) {
            return new MarketNewsSlotDTO();
        }

        final MarketNewsSlotDTO marketNewsSlotDTO = new MarketNewsSlotDTO();

        marketNewsSlotDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        marketNewsSlotDTO.setTime(entity.getTime());
        marketNewsSlotDTO.setEntries(this.marketNewsEntryDTOConverter.convertAll(entity.getEntries()));

        return marketNewsSlotDTO;
    }
}
