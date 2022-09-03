package com.stephenprizio.traderbuddy.models.records.reporting;

import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

/**
 * Represents a summary of trades for a particular time span
 *
 * @param date             {@link LocalDateTime} of period
 * @param target           target goal for profit
 * @param numberOfTrades   number of trades taken
 * @param winPercentage    number of trades won expressed as a percentage between 0 & 100
 * @param netProfit        net profit of trades (can be negative)
 * @param percentageProfit profit as a percentage of target
 * @param surplus          net difference between target and profit
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record TradingRecord(LocalDateTime date, Double target, Integer numberOfTrades, Integer winPercentage, Double netProfit, Double percentageProfit, Double surplus, Boolean show) {

    /**
     * Determines whether this record is empty
     *
     * @return true if weekend or no trades taken
     */
    public Boolean isEmpty() {
        if (Boolean.TRUE.equals(this.show)) {
            return false;
        }

        return isWeekend() || this.numberOfTrades == 0;
    }

    /**
     * Determines whether this record is a weekend entry
     *
     * @return true if weekend entry
     */
    public Boolean isWeekend() {
        if (Boolean.TRUE.equals(this.show)) {
            return false;
        }

        return this.date.get(ChronoField.DAY_OF_WEEK) > 5;
    }
}
