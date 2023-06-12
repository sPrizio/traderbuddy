package com.traderbuddyv2.api.converters.trade;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.converters.account.AccountDTOConverter;
import com.traderbuddyv2.api.models.dto.trade.TradeDTO;
import com.traderbuddyv2.api.models.records.wrapper.TradeReasonWrapper;
import com.traderbuddyv2.api.models.records.wrapper.TradeResultWrapper;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.trade.platform.TradePlatform;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.ZoneId;

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
        tradeDTO.setResultOfTrade(entity.getResultsOfTrade().stream().map(TradeResultWrapper::new).toList());
        tradeDTO.setTradeType(entity.getTradeType());
        tradeDTO.setOpenPrice(entity.getOpenPrice());
        tradeDTO.setClosePrice(entity.getClosePrice());

        if (entity.getAccount().getTradePlatform().equals(TradePlatform.METATRADER4)) {
            tradeDTO.setTradeOpenTime(entity.getTradeOpenTime().atZone(ZoneId.of(CoreConstants.METATRADER4_TIMEZONE)).withZoneSameInstant(ZoneId.of(getZone(entity.getAccount()))).toLocalDateTime());
            tradeDTO.setTradeCloseTime(entity.getTradeCloseTime().atZone(ZoneId.of(CoreConstants.METATRADER4_TIMEZONE)).withZoneSameInstant(ZoneId.of(getZone(entity.getAccount()))).toLocalDateTime());
        } else {
            tradeDTO.setTradeOpenTime(entity.getTradeOpenTime());
            tradeDTO.setTradeCloseTime(entity.getTradeCloseTime());
        }

        tradeDTO.setLotSize(entity.getLotSize());
        tradeDTO.setNetProfit(entity.getNetProfit());
        tradeDTO.setPips(Math.abs(this.mathService.subtract(entity.getOpenPrice(), entity.getClosePrice())));
        tradeDTO.setReasonForEntrance(entity.getReasonsForEntry().stream().map(TradeReasonWrapper::new).toList());
        tradeDTO.setRelevant(entity.isRelevant());
        tradeDTO.setProcessed(entity.isProcessed());
        tradeDTO.setStopLoss(entity.getStopLoss());
        tradeDTO.setTakeProfit(entity.getTakeProfit());
        tradeDTO.setAccount(this.accountDTOConverter.convert(entity.getAccount()));


        return tradeDTO;
    }


    //  HELPERS

    /**
     * Obtains the timezone based on the user's locale
     *
     * @param account {@link Account}
     * @return user's timezone
     */
    private String getZone(final Account account) {

        if (account != null && account.getUser() != null && account.getUser().getUserLocale() != null) {
            final String offset = account.getUser().getUserLocale().getTimeZoneOffset();
            if (StringUtils.isNotEmpty(offset)) {
                final String[] array = offset.split(" ");
                if (array.length > 0) {
                    return array[0];
                }
            }
        }

        return CoreConstants.EASTERN_TIMEZONE;
    }
}
