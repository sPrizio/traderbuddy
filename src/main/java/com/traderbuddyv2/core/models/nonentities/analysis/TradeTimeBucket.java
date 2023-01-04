package com.traderbuddyv2.core.models.nonentities.analysis;

import com.traderbuddyv2.core.models.entities.trade.Trade;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

/**
 * Class representation of a breakdown of trades for a given time span
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradeTimeBucket extends Analysis {

    @Getter
    @Setter
    private LocalTime start;

    @Getter
    @Setter
    private LocalTime end;

    @Getter
    @Setter
    private double pips;

    @Getter
    @Setter
    private double netProfit;

    @Getter
    @Setter
    private long trades;

    @Getter
    @Setter
    private long winningTrades;

    @Getter
    @Setter
    private long losingTrades;

    @Getter
    @Setter
    private int winPercentage;


    //  CONSTRUCTORS

    public TradeTimeBucket(final LocalTime start, final LocalTime end, final List<Trade> trades) {
        this.start = start;
        this.end = end;
        this.pips = trades.stream().mapToDouble(this::getPips).sum();
        this.netProfit = trades.stream().mapToDouble(Trade::getNetProfit).sum();
        this.trades = trades.size();
        this.winningTrades = trades.stream().filter(trade -> trade.getNetProfit() >= 0.0).count();
        this.losingTrades = trades.stream().filter(trade -> trade.getNetProfit() < 0.0).count();
        this.winPercentage = 0;
    }
}
