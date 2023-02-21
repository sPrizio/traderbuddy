package com.traderbuddyv2.core.models.nonentities.analysis.performance;

import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.models.nonentities.analysis.Analysis;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Class representation of a {@link Trade}'s performance. Used for analyzing trades and looking for insights
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradePerformance extends Analysis {

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
}
