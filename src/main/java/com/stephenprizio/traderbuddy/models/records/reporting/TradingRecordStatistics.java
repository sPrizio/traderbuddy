package com.stephenprizio.traderbuddy.models.records.reporting;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Represents a collection of statistics for a collection of {@link TradingRecord}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record TradingRecordStatistics(List<TradingRecord> records) {


    //  METHODS

    public Integer getTotalNumberOfTrades() {
        return this.records.stream().mapToInt(TradingRecord::numberOfTrades).sum();
    }

    public Integer getAverageWinPercentage() {
        OptionalDouble average = this.records.stream().mapToInt(TradingRecord::winPercentage).average();
        return BigDecimal.valueOf(average.orElse(0.0)).setScale(0, RoundingMode.HALF_EVEN).intValue();
    }

    public Double getNetProfit() {
        double sum = this.records.stream().mapToDouble(TradingRecord::netProfit).sum();
        return BigDecimal.valueOf(sum).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }
}
