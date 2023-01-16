package com.traderbuddyv2.core.models.nonentities.analysis;

import lombok.Getter;

/**
 * Class implementation of a trading day breakdown
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
public record TradeRecordPerformanceBucket(
        @Getter
        int start,
        @Getter
        int end,
        @Getter
        int count
) { }
