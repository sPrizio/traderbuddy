package com.stephenprizio.traderbuddy.models.records.reporting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;

/**
 * Represents a collection of statistics for a collection of {@link TradingRecord}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record TradingRecordStatistics(List<TradingRecord> records) {


    //  METHODS

    /**
     * Obtains the sum of numberOfTrades
     *
     * @return {@link Integer}
     */
    public Integer getTotalNumberOfTrades() {
        return
                this.records
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(r -> !r.isEmpty())
                        .mapToInt(TradingRecord::numberOfTrades)
                        .sum();
    }

    /**
     * Obtains the average of winPercentage (rounded to the nearest whole number)
     *
     * @return {@link Integer}
     */
    public Integer getAverageWinPercentage() {

        OptionalDouble average =
                this.records.
                        stream()
                        .filter(Objects::nonNull)
                        .filter(r -> !r.isEmpty())
                        .mapToInt(TradingRecord::winPercentage)
                        .average();

        return
                BigDecimal
                        .valueOf(average.orElse(0.0))
                        .setScale(0, RoundingMode.HALF_EVEN)
                        .intValue();
    }

    /**
     * Obtains the sum of netProfit
     *
     * @return {@link Double}
     */
    public Double getNetProfit() {

        double sum =
                this.records
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(r -> !r.isEmpty())
                        .mapToDouble(TradingRecord::netProfit)
                        .sum();

        return BigDecimal
                .valueOf(sum)
                .setScale(2, RoundingMode.HALF_EVEN)
                .doubleValue();
    }

    /**
     * Obtains the average of profitPercentage
     *
     * @return {@link Double}
     */
    public Double getAverageProfitPercentage() {

        OptionalDouble average =
                this.records.
                        stream()
                        .filter(Objects::nonNull)
                        .filter(r -> !r.isEmpty())
                        .filter(TradingRecord::isCompletedSession)
                        .mapToDouble(TradingRecord::percentageProfit)
                        .average();

        return
                BigDecimal
                        .valueOf(average.orElse(0.0))
                        .setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue();
    }

    /**
     * Obtains the sum of surplus
     *
     * @return {@link Double}
     */
    public Double getSurplus() {
        return
                BigDecimal.valueOf(
                                this.records
                                        .stream()
                                        .filter(Objects::nonNull)
                                        .filter(TradingRecord::isCompletedSession)
                                        .mapToDouble(TradingRecord::surplus)
                                        .sum()
                        ).setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue();
    }
}
