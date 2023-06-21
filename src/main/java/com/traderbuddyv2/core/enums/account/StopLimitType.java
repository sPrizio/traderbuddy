package com.traderbuddyv2.core.enums.account;

import lombok.Getter;

/**
 * Class representation of a type of stop limit
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum StopLimitType {
    PROFIT("Profit"),
    POINTS("Points");

    @Getter
    private final String label;

    StopLimitType(final String label) {
        this.label = label;
    }
}
