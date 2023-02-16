package com.traderbuddyv2.api.models.records.wrapper;

import com.traderbuddyv2.core.enums.trade.tag.TradeResult;
import lombok.Getter;

/**
 * Wrapper for {@link TradeResult}
 *
 * @param code code
 * @param label label
 * @param description description
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record TradeResultWrapper(
        @Getter String code,
        @Getter String label,
        @Getter String description
) {
    public TradeResultWrapper(final TradeResult result) {
        this(result.getCode(), result.getLabel(), result.getDescription());
    }
}
