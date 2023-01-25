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

    NONE("None", 0),
    DANGEROUS("Dangerous", 1),
    MODERATE("Moderate", 2),
    LOW("Low", 3);

    @Getter
    private final String description;

    @Getter
    private final int level;

    MarketNewsSeverity(final String description, final int level) {
        this.description = description;
        this.level = level;
    }

    /**
     * Converts a level to a {@link MarketNewsSeverity}
     *
     * @param level level
     * @return {@link MarketNewsSeverity}
     */
    public static MarketNewsSeverity get(final int level) {
        return switch (level) {
            case 1 -> DANGEROUS;
            case 2 -> MODERATE;
            case 3 -> LOW;
            default -> NONE;
        };
    }
}
