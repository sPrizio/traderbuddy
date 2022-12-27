package com.traderbuddyv2.core.models.nonentities.analysis;

import com.traderbuddyv2.core.enums.trades.TradeType;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Class representation of a {@link Trade}'s performance. Used for analyzing trades and looking for insights
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradePerformance {

    @Getter
    @Setter
    private LocalDateTime tradeOpenTime;

    @Getter
    @Setter
    private LocalDateTime tradeCloseTime;

    @Getter
    @Setter
    private double netProfit;

    @Getter
    @Setter
    private double pips;

    @Getter
    @Setter
    private long tradeDuration;

    @Getter
    @Setter
    private double lotSize;

    @Getter
    @Setter
    private String product;

    @Getter
    @Setter
    private boolean loss;


    //  CONSTRUCTORS

    public TradePerformance(final Trade trade) {
        this.tradeOpenTime = trade.getTradeOpenTime();
        this.tradeCloseTime = trade.getTradeCloseTime();
        this.loss = trade.getNetProfit() < 0;
        this.netProfit = trade.getNetProfit();
        this.pips = getPips(trade);
        this.tradeDuration = Math.abs(ChronoUnit.SECONDS.between(trade.getTradeOpenTime(), trade.getTradeCloseTime()));
        this.lotSize = trade.getLotSize();
        this.product = trade.getProduct();
    }


    //  HELPERS

    /**
     * Obtains the number of points collected by the {@link Trade}. In this case this refers to the absolute value of
     * the difference of the open and close price
     *
     * @param trade {@link Trade}
     * @return {@link Double}
     */
    private double getPips(final Trade trade) {

        if (trade.getTradeType().equals(TradeType.BUY)) {
            return BigDecimal.valueOf(trade.getClosePrice()).subtract(BigDecimal.valueOf(trade.getOpenPrice())).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        } else {
            return BigDecimal.valueOf(trade.getOpenPrice()).subtract(BigDecimal.valueOf(trade.getClosePrice())).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        }
    }
}
