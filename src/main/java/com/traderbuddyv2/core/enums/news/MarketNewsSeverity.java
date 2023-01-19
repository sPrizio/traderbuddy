package com.traderbuddyv2.core.enums.news;

import lombok.Getter;

/**
 * Enum representing possible severity danger/warning levels for news, indicating their overall likelihood
 * of moving the market in a significant direction
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum MarketNewsSeverity {

    LOW("Low", 3),
    MODERATE("Moderate", 2),
    DANGEROUS("Dangerous", 1);

    @Getter
    private final String description;

    @Getter
    private final int level;

    MarketNewsSeverity(final String description, final int level) {
        this.description = description;
        this.level = level;
    }
}
