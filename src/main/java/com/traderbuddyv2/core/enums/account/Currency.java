package com.traderbuddyv2.core.enums.account;

import lombok.Getter;

/**
 * Enum representing a standard currency
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum Currency {
    US_DOLLAR("USD"),
    EUROPEAN_EURO("EUR"),
    JAPANESE_YEN("JPY"),
    BRITISH_POUND("GBP"),
    SWISS_FRANC("CHF"),
    CANADIAN_DOLLAR("CAD"),
    AUSTRALIAN_DOLLAR("AUD"),
    NEW_ZEALAND_DOLLAR("NZD"),
    SOUTH_AFRICAN_RAND("ZAR");

    @Getter
    private final String isoCode;

    Currency(final String isoCode) {
        this.isoCode = isoCode;
    }
}
