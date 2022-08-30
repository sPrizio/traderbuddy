package com.stephenprizio.traderbuddy.models.records.reporting;

import java.util.List;

/**
 * Represents a summary of {@link TradingRecord}s
 *
 * @param records {@link List} of {@link TradingRecord}s
 * @param statistics {@link TradingRecordStatistics}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record TradingSummary(List<TradingRecord> records, TradingRecordStatistics statistics) {
}
