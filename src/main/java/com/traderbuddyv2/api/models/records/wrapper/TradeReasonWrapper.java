package com.traderbuddyv2.api.models.records.wrapper;

import com.traderbuddyv2.core.enums.trade.info.TradeDirection;
import com.traderbuddyv2.core.enums.trade.tag.TradeEntryReason;
import lombok.Getter;

/**
 * A wrapper for {@link TradeEntryReason} for use in DTOs
 *
 * @param code code
 * @param label label
 * @param direction description
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record TradeReasonWrapper(
        @Getter String code,
        @Getter String label,
        @Getter TradeDirection direction
) {
    public TradeReasonWrapper(final TradeEntryReason reason) {
        this(reason.getCode(), reason.getLabel(), reason.getDirection());
    }
}
