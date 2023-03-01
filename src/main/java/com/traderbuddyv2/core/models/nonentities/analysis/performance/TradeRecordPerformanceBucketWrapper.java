package com.traderbuddyv2.core.models.nonentities.analysis.performance;

import lombok.Getter;

import java.util.List;

/**
 * Wrapper class to hold extra info
 *
 * @param buckets {@link List} of {@link TradeRecordPerformanceBucket}
 * @param statistics {@link TradeRecordPerformanceBucketStatistics}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record TradeRecordPerformanceBucketWrapper(@Getter List<TradeRecordPerformanceBucket> buckets, @Getter TradeRecordPerformanceBucketStatistics statistics) {
}
