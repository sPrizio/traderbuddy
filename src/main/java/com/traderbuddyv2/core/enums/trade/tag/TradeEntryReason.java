package com.traderbuddyv2.core.enums.trade.tag;

import com.traderbuddyv2.core.enums.trade.info.TradeDirection;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import lombok.Getter;

/**
 * Enumeration of a tag, a piece of meta information that we can apply to a {@link Trade} representing the reason for entering a {@link Trade}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum TradeEntryReason {
    DOUBLE_TOP("dt", "Double Top", TradeDirection.BEARISH),
    DOUBLE_BOTTOM("db", "Double Bottom", TradeDirection.BULLISH),
    MULTIPLE_TOP("mt", "Multiple Top", TradeDirection.BEARISH),
    MULTIPLE_BOTTOM("mb", "Multiple Bottom", TradeDirection.BULLISH),
    PULLBACK_DOWNTREND("p_bear", "Pullback (Downtrend)", TradeDirection.BEARISH),
    PULLBACK_UPTREND("p_bull", "Pullback (Uptrend)", TradeDirection.BULLISH),
    BREAKOUT_DOWNWARD("brk_bear", "Breakout (Downward)", TradeDirection.BEARISH),
    BREAKOUT_UPWARD("brk_bull", "Breakout (Upward)", TradeDirection.BULLISH),
    FAILED_BREAKOUT_UPWARD("f_brk_bull", "Failed Breakout (Upward)", TradeDirection.BEARISH),
    FAILED_BREAKOUT_DOWNWARD("f_brk_bear", "Failed Breakout (Downward)", TradeDirection.BULLISH);

    @Getter
    private final String code;

    @Getter
    private final String label;

    @Getter
    private final TradeDirection direction;

    TradeEntryReason(final String code, final String label, final TradeDirection direction) {
        this.code = code;
        this.label = label;
        this.direction = direction;
    }
}
