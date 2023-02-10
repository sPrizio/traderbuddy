package com.traderbuddyv2.core.enums.account;

import lombok.Getter;

/**
 * Enum representing a standard currency
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum Currency {
    US_DOLLAR("USD", "US Dollars"),
    EUROPEAN_EURO("EUR", "Euros"),
    JAPANESE_YEN("JPY", "Japanese Yen"),
    BRITISH_POUND("GBP", "British Pounds"),
    SWISS_FRANC("CHF", "Swiss Francs"),
    CANADIAN_DOLLAR("CAD", "Canadian Dollars"),
    AUSTRALIAN_DOLLAR("AUD", "Australian Dollars"),
    NEW_ZEALAND_DOLLAR("NZD", "New Zealand Dollars"),
    SOUTH_AFRICAN_RAND("ZAR", "South African Rand");

    @Getter
    private final String isoCode;

    @Getter
    private final String label;

    Currency(final String isoCode, final String label) {
        this.isoCode = isoCode;
        this.label = label;
    }
}
