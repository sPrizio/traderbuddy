package com.traderbuddyv2.core.models.nonentities.analysis.bucket;

import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.models.nonentities.analysis.Analysis;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Parent-level class for buckets
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public abstract class Bucket extends Analysis {

    @Getter
    @Setter
    private double pips;

    @Getter
    @Setter
    private double pipsEarned;

    @Getter
    @Setter
    private double pipsLost;

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

    Bucket(final List<Trade> trades) {
        this.pips = trades.stream().mapToDouble(this::getPips).sum();
        this.pipsEarned = trades.stream().mapToDouble(this::getPips).filter(p -> p >= 0.0).sum();
        this.pipsLost = trades.stream().mapToDouble(this::getPips).filter(p -> p < 0.0).sum() * -1;
        this.netProfit = BigDecimal.valueOf(trades.stream().mapToDouble(Trade::getNetProfit).sum()).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        this.trades = trades.size();
        this.winningTrades = trades.stream().filter(trade -> trade.getNetProfit() >= 0.0).count();
        this.losingTrades = trades.stream().filter(trade -> trade.getNetProfit() < 0.0).count();
        this.winPercentage = 0;
    }
}
