package com.stephenprizio.traderbuddy.models.records.reporting.trades;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;

/**
 * Represents a summary of trades for a particular time span
 *
 * @param start            start {@link LocalDateTime} of period
 * @param end              end {@link LocalDateTime} of period
 * @param target           target goal for profit
 * @param numberOfTrades   number of trades taken
 * @param winPercentage    number of trades won expressed as a percentage between 0 & 100
 * @param netProfit        net profit of trades (can be negative)
 * @param percentageProfit profit as a percentage of target
 * @param surplus          net difference between target and profit
 * @author Stephen Prizio
 * @version 1.0
 */
public record TradingRecord(LocalDateTime start, LocalDateTime end, Double target, Integer numberOfTrades, Integer winPercentage, Double netProfit, Double percentageProfit, Double surplus, Boolean show, Boolean targetHit) {

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

        return this.start.get(ChronoField.DAY_OF_WEEK) > 5;
    }

    /**
     * Returns true if the current date is between the start and end date
     *
     * @return true if current date > start date and current date < end date
     */
    public Boolean getActive() {
        LocalDate now = LocalDate.now();
        return (now.isAfter(this.start.toLocalDate()) || now.isEqual(this.start.toLocalDate())) && (now.isBefore(this.end.toLocalDate()));
    }

    /**
     * Returns true if the record is completed
     *
     * @return true if inactive
     */
    public Boolean isCompletedSession() {
        LocalDate now = LocalDate.now();
        return !getActive() && this.end.toLocalDate().isBefore(now);
    }
}
