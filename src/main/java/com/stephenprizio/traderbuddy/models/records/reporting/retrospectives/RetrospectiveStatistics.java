package com.stephenprizio.traderbuddy.models.records.reporting.retrospectives;

import com.stephenprizio.traderbuddy.models.entities.retrospectives.Retrospective;

/**
 * Class representation of statistics for {@link Retrospective}s
 *
 * @param totalTrades total trades
 * @param tradingRate trades / days
 * @param winPercentage winning trades / total trades (rounded to the nearest integer)
 * @param netProfit total profit
 * @param averageGain average percentage gain
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record RetrospectiveStatistics(Integer totalTrades, Double tradingRate, Integer winPercentage, Double netProfit, Double averageGain) {
}
