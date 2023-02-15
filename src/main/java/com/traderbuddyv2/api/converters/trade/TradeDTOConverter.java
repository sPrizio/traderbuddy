package com.traderbuddyv2.api.converters.trade;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.converters.account.AccountDTOConverter;
import com.traderbuddyv2.api.models.dto.trade.TradeDTO;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Converter for {@link Trade}s into {@link TradeDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("tradeDTOConverter")
public class TradeDTOConverter implements GenericDTOConverter<Trade, TradeDTO> {

    @Resource(name = "accountDTOConverter")
    private AccountDTOConverter accountDTOConverter;

    @Resource(name = "mathService")
    private MathService mathService;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public TradeDTO convert(final Trade entity) {

        if (entity == null) {
            return new TradeDTO();
        }

        final TradeDTO tradeDTO = new TradeDTO();

        tradeDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        tradeDTO.setTradeId(entity.getTradeId());
        tradeDTO.setTradePlatform(entity.getTradePlatform());
        tradeDTO.setProduct(entity.getProduct());
        tradeDTO.setResultOfTrade(entity.getResultOfTrade());
        tradeDTO.setTradeType(entity.getTradeType());
        tradeDTO.setOpenPrice(entity.getOpenPrice());
        tradeDTO.setClosePrice(entity.getClosePrice());
        tradeDTO.setTradeOpenTime(entity.getTradeOpenTime());
        tradeDTO.setTradeCloseTime(entity.getTradeCloseTime());
        tradeDTO.setLotSize(entity.getLotSize());
        tradeDTO.setNetProfit(entity.getNetProfit());
        tradeDTO.setPips(Math.abs(this.mathService.subtract(entity.getOpenPrice(), entity.getClosePrice())));
        tradeDTO.setReasonForEntrance(entity.getReasonForEntrance());
        tradeDTO.setRelevant(entity.isRelevant());
        tradeDTO.setProcessed(entity.isProcessed());
        tradeDTO.setAccount(this.accountDTOConverter.convert(entity.getAccount()));


        return tradeDTO;
    }
}
