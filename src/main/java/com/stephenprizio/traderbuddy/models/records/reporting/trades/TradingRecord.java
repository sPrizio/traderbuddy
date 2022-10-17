package com.stephenprizio.traderbuddy.models.records.reporting.trades;

import com.stephenprizio.traderbuddy.models.entities.trades.Trade;
import org.apache.commons.collections4.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.List;

/**
 * Represents a summary of trades for a particular time span
 *
 * @param trades           {@link List} of {@link Trade}s
 * @param start            start {@link LocalDateTime} of period
 * @param end              end {@link LocalDateTime} of period
 * @param target           target goal for profit
 * @param percentageProfit profit as a percentage of target
 * @param surplus          net difference between target and profit
 * @param show             should show this record
 * @param targetHit        was the target (from a forecast) hit?
 * @author Stephen Prizio
 * @version 1.0
 */
public record TradingRecord(
        List<Trade> trades,
        LocalDateTime start,
        LocalDateTime end,
        Double target,
        Double percentageProfit,
        Double surplus,
        Boolean show,
        Boolean targetHit
) {

    /**
     * Determines whether this record is empty
     *
     * @return true if weekend or no trades taken
     */
    public Boolean isEmpty() {

        if (Boolean.TRUE.equals(this.show)) {
            return false;
        }

        return isWeekend() || CollectionUtils.isEmpty(this.trades);
    }

    /**
     * Determines whether this record is not empty
     *
     * @return true if not a weekend and trades were taken
     */
    public Boolean isNotEmpty() {
        return !isEmpty();
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

    /**
     * Obtains a count of all trades
     *
     * @return {@link Integer}
     */
    public Integer getTotalNumberOfTrades() {

        if (CollectionUtils.isNotEmpty(this.trades)) {
            return this.trades.size();
        }

        return 0;
    }

    /**
     * Obtains a count of all trades whose net profit is >= 0
     *
     * @return {@link Integer}
     */
    public Integer getTotalNumberOfWinningTrades() {

        if (CollectionUtils.isNotEmpty(this.trades)) {
            return (int) this.trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d >= 0).count();
        }

        return 0;
    }

    /**
     * Obtains a count of all trades whose net profit is < 0
     *
     * @return {@link Integer}
     */
    public Integer getTotalNumberOfLosingTrades() {

        if (CollectionUtils.isNotEmpty(this.trades)) {
            return (int) this.trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d < 0).count();
        }

        return 0;
    }

    /**
     * Returns the winning percentage rounded to the nearest integer
     *
     * @return {@link Integer}
     */
    public Integer getWinPercentage() {

        if (getTotalNumberOfTrades() == 0) {
            return 0;
        }

        return
                BigDecimal.valueOf(getTotalNumberOfWinningTrades())
                        .divide(BigDecimal.valueOf(getTotalNumberOfTrades()), 10, RoundingMode.HALF_EVEN)
                        .multiply(BigDecimal.valueOf(100.0))
                        .setScale(0, RoundingMode.HALF_EVEN)
                        .intValue();
    }

    /**
     * Returns the sum of each trade's profit/loss
     *
     * @return {@link Double}
     */
    public Double getNetProfit() {

        if (CollectionUtils.isNotEmpty(this.trades)) {
            return BigDecimal.valueOf(this.trades.stream().mapToDouble(Trade::getNetProfit).sum()).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
        }

        return 0.0;
    }
}
