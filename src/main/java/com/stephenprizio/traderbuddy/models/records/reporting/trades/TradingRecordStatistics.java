package com.stephenprizio.traderbuddy.models.records.reporting.trades;

import com.stephenprizio.traderbuddy.models.entities.trades.Trade;
import org.apache.commons.collections4.CollectionUtils;

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
                        .mapToInt(TradingRecord::getTotalNumberOfTrades)
                        .sum();
    }

    /**
     * Obtains the sum of winningTrades
     *
     * @return {@link Integer}
     */
    public Integer getTotalNumberOfWinningTrades() {
        return
                this.records
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(r -> !r.isEmpty())
                        .mapToInt(TradingRecord::getTotalNumberOfWinningTrades)
                        .sum();
    }

    /**
     * Obtains the sum of losingTrades
     *
     * @return {@link Integer}
     */
    public Integer getTotalNumberOfLosingTrades() {
        return
                this.records
                        .stream()
                        .filter(Objects::nonNull)
                        .filter(r -> !r.isEmpty())
                        .mapToInt(TradingRecord::getTotalNumberOfLosingTrades)
                        .sum();
    }

    /**
     * Obtains the number of trades per record unit
     *
     * @return {@link Double}
     */
    public Double getTradingRate() {
        return
                BigDecimal.valueOf(getTotalNumberOfTrades())
                        .divide(BigDecimal.valueOf(this.records.stream().filter(Objects::nonNull).filter(r -> !r.isEmpty()).count()), 10, RoundingMode.HALF_EVEN)
                        .setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue();

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
                        .mapToInt(TradingRecord::getWinPercentage)
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
                        .mapToDouble(TradingRecord::getNetProfit)
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

    /**
     *
     *
     * @return {@link Double}
     */
    public Double getAverageWinSize() {

        List<Trade> trades = getTrades();
        if (CollectionUtils.isEmpty(trades)) {
            return 0.0;
        }

        return
                BigDecimal
                        .valueOf(
                                trades
                                        .stream()
                                        .filter(trade -> trade.getNetProfit() >= 0.0)
                                        .mapToDouble(Trade::getLotSize)
                                        .average()
                                        .orElse(0.0)
                        )
                        .setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue();
    }

    /**
     *
     *
     * @return {@link Double}
     */
    public Double getLargestWinSize() {

        List<Trade> trades = getTrades();
        if (CollectionUtils.isEmpty(trades)) {
            return 0.0;
        }

        return
                BigDecimal
                        .valueOf(
                                trades
                                        .stream()
                                        .filter(trade -> trade.getNetProfit() >= 0.0)
                                        .mapToDouble(Trade::getLotSize)
                                        .max()
                                        .orElse(0.0)
                        )
                        .setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue();
    }

    /**
     *
     *
     * @return {@link Double}
     */
    public Double getAverageLossSize() {

        List<Trade> trades = getTrades();
        if (CollectionUtils.isEmpty(trades)) {
            return 0.0;
        }

        return
                BigDecimal
                        .valueOf(
                                trades
                                        .stream()
                                        .filter(trade -> trade.getNetProfit() < 0.0)
                                        .mapToDouble(Trade::getLotSize)
                                        .average()
                                        .orElse(0.0)
                        )
                        .setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue();
    }

    /**
     *
     *
     * @return {@link Double}
     */
    public Double getLargestLossSize() {

        List<Trade> trades = getTrades();
        if (CollectionUtils.isEmpty(trades)) {
            return 0.0;
        }

        return
                BigDecimal
                        .valueOf(
                                trades
                                        .stream()
                                        .filter(trade -> trade.getNetProfit() < 0.0)
                                        .mapToDouble(Trade::getLotSize)
                                        .max()
                                        .orElse(0.0)
                        )
                        .setScale(2, RoundingMode.HALF_EVEN)
                        .doubleValue();
    }


    //  HELPERS

    /**
     * Obtains a {@link List} of {@link Trade}s
     *
     * @return {@link List} of {@link Trade}s
     */
    private List<Trade> getTrades() {
        return this.records.stream().map(TradingRecord::trades).flatMap(List::stream).toList();
    }
}
