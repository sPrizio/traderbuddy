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
    WEAKNESS("w", "Weakness", TradeDirection.BEARISH),
    STRENGTH("str", "Strength", TradeDirection.BULLISH),
    RESISTANCE("r", "Resistance", TradeDirection.BEARISH),
    SUPPORT("s", "Support", TradeDirection.BULLISH),
    DOUBLE_TOP("dt", "Double Top", TradeDirection.BEARISH),
    DOUBLE_BOTTOM("db", "Double Bottom", TradeDirection.BULLISH),
    HEAD_AND_SHOULDERS("hs", "Head and Shoulders", TradeDirection.BEARISH),
    INVERSE_HEAD_AND_SHOULDERS("ihs", "Inverse Head and Shoulders", TradeDirection.BULLISH),
    PULLBACK_DOWNTREND("p_bear", "Downtrend Pullback", TradeDirection.BEARISH),
    PULLBACK_UPTREND("p_bull", "Uptrend Pullback", TradeDirection.BULLISH),
    BREAKOUT_DOWNWARD("brk_bear", "Downward Breakout", TradeDirection.BEARISH),
    BREAKOUT_UPWARD("brk_bull", "Upward Breakout", TradeDirection.BULLISH),
    FAILED_BREAKOUT_UPWARD("f_brk_bear", "Failed Upward Breakout", TradeDirection.BEARISH),
    FAILED_BREAKOUT_DOWNWARD("f_brk_bull", "Failed Downward Breakout", TradeDirection.BULLISH);

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
