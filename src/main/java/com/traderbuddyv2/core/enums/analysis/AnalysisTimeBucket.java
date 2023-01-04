package com.traderbuddyv2.core.enums.analysis;

import lombok.Getter;

/**
 * Enum representing a time bucket of trades
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum AnalysisTimeBucket {

    ONE_MINUTE("1m", 1),
    FIVE_MINUTES("5m", 5),
    FIFTEEN_MINUTES("15m", 15),
    THIRTY_MINUTES("30m", 30),
    ONE_HOUR("1h", 60);

    @Getter
    private final String code;

    @Getter
    private final int minutes;

    AnalysisTimeBucket(final String code, final int minutes) {
        this.code = code;
        this.minutes = minutes;
    }

    /**
     * Converts a code to an {@link AnalysisTimeBucket}
     *
     * @param code code
     * @return {@link AnalysisTimeBucket}
     */
    public static AnalysisTimeBucket get(final String code) {
        return switch (code) {
            case "1m" -> ONE_MINUTE;
            case "5m" -> FIVE_MINUTES;
            case "15m" -> FIFTEEN_MINUTES;
            case "30m" -> THIRTY_MINUTES;
            case "1h" -> ONE_HOUR;
            default -> null;
        };
    }
}
