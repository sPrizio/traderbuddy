package com.stephenprizio.traderbuddy.converters.trades;

import com.stephenprizio.traderbuddy.converters.GenericDTOConverter;
import com.stephenprizio.traderbuddy.models.dto.trades.TradeDTO;
import com.stephenprizio.traderbuddy.models.entities.trades.Trade;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converter for {@link Trade}s into {@link TradeDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("tradeDTOConverter")
public class TradeDTOConverter implements GenericDTOConverter<Trade, TradeDTO> {


    //  METHODS

    @Override
    public TradeDTO convert(final Trade entity) {

        if (entity == null) {
            return new TradeDTO();
        }

        TradeDTO tradeDTO = new TradeDTO();

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

    @Override
    public List<TradeDTO> convertAll(final List<Trade> entities) {
        return entities.stream().map(this::convert).toList();
    }
}
