package com.traderbuddyv2.core.models.nonentities.analysis;

import lombok.Getter;
import lombok.Setter;

/**
 * Class representation of an average of trade performances
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class AverageTradePerformance extends Analysis {

    @Getter
    @Setter
    private double averageProfit;

    @Getter
    @Setter
    private double averagePips;

    @Getter
    @Setter
    private double totalPips;

    @Getter
    @Setter
    private double averageLotSize;

    @Getter
    @Setter
    private long averageTradeDuration;

    @Getter
    @Setter
    private double winLossPercentage;

    @Getter
    @Setter
    private int numberOfWinLosses;

    @Getter
    @Setter
    private int totalTrades;

    @Getter
    @Setter
    private double tradingRate;

    @Getter
    @Setter
    private long totalAverageDuration;

    @Getter
    @Setter
    private double profitability;
}
