package com.traderbuddyv2.core.enums.trade.info;

import com.traderbuddyv2.core.models.entities.trade.Trade;
import lombok.Getter;

/**
 * Enumeration representing the direction of a {@link Trade}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum TradeDirection {

    BULLISH("Bullish", "Upward"),
    BEARISH("Bearish", "Downward");

    @Getter
    private final String label;

    @Getter
    private final String description;

    TradeDirection(final String label, final String description) {
        this.label = label;
        this.description = description;
    }
}
