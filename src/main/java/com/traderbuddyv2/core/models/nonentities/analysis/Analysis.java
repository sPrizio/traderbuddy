package com.traderbuddyv2.core.models.nonentities.analysis;

import com.traderbuddyv2.core.enums.trades.TradeType;
import com.traderbuddyv2.core.models.entities.trade.Trade;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Abstract parent class for analysis classes
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public abstract class Analysis {

    /**
     * Obtains the number of points collected by the {@link Trade}. In this case this refers to the absolute value of
     * the difference of the open and close price
     *
     * @param trade {@link Trade}
     * @return {@link Double}
     */
    public double getPips(final Trade trade) {
        if (trade.getTradeType().equals(TradeType.BUY)) {
            return BigDecimal.valueOf(trade.getClosePrice()).subtract(BigDecimal.valueOf(trade.getOpenPrice())).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        } else {
            return BigDecimal.valueOf(trade.getOpenPrice()).subtract(BigDecimal.valueOf(trade.getClosePrice())).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        }
    }
}
