package com.stephenprizio.traderbuddy.converters.trades;

import com.stephenprizio.traderbuddy.converters.GenericDTOConverter;
import com.stephenprizio.traderbuddy.models.dto.trades.TradeDTO;
import com.stephenprizio.traderbuddy.models.entities.trades.Trade;
import com.stephenprizio.traderbuddy.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Converter for {@link Trade}s into {@link TradeDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("tradeDTOConverter")
public class TradeDTOConverter implements GenericDTOConverter<Trade, TradeDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public TradeDTO convert(final Trade entity) {

        if (entity == null) {
            return new TradeDTO();
        }

        TradeDTO tradeDTO = new TradeDTO();

        tradeDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        tradeDTO.setTradeId(entity.getTradeId());
        tradeDTO.setTradingPlatform(entity.getTradingPlatform());
        tradeDTO.setProduct(entity.getProduct());
        tradeDTO.setResultOfTrade(entity.getResultOfTrade());
        tradeDTO.setTradeType(entity.getTradeType());
        tradeDTO.setOpenPrice(entity.getOpenPrice());
        tradeDTO.setClosePrice(entity.getClosePrice());
        tradeDTO.setTradeOpenTime(entity.getTradeOpenTime());
        tradeDTO.setTradeCloseTime(entity.getTradeCloseTime());
        tradeDTO.setLotSize(entity.getLotSize());
        tradeDTO.setNetProfit(entity.getNetProfit());
        tradeDTO.setReasonForEntrance(entity.getReasonForEntrance());
        tradeDTO.setRelevant(entity.getRelevant());

        return tradeDTO;
    }
}
