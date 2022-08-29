package com.stephenprizio.traderbuddy.models.nonentities.statistics;

import com.stephenprizio.traderbuddy.models.records.reporting.TradingRecord;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.OptionalDouble;

/**
 * Represents a collection of statistics for a collection of {@link TradingRecord}s
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
public class TradingRecordStatistics {

    private final List<TradingRecord> records;

    public TradingRecordStatistics(List<TradingRecord> records) {
        this.records = records;
    }


    //  METHODS

    public int getTotalNumberOfTrades() {
        return this.records.stream().mapToInt(TradingRecord::numberOfTrades).sum();
    }

    public int getAverageWinPercentage() {
        OptionalDouble average = this.records.stream().mapToInt(TradingRecord::winPercentage).average();
        return BigDecimal.valueOf(average.orElse(0.0)).setScale(0, RoundingMode.HALF_EVEN).intValue();
    }

    public double getNetProfit() {
        return this.records.stream().mapToDouble(TradingRecord::netProfit).sum();
    }
}