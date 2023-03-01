package com.traderbuddyv2.core.models.nonentities.analysis.performance;

import lombok.Getter;

/**
 * Stats info
 *
 * @param average average
 * @param count count
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record TradeRecordPerformanceBucketStatistics(@Getter double average, @Getter int count) {
}
