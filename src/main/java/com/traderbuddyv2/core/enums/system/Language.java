package com.traderbuddyv2.core.enums.system;

import lombok.Getter;

/**
 * Enumeration of various languages
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum Language {

    ENGLISH("EN", "English"),
    FRENCH("FR", "French"),
    SPANISH("ES", "Spanish"),
    GERMAN("DE", "German"),
    CHINESE("ZH", "Chinese"),
    ITALIAN("IT", "Italian"),
    PORTUGUESE("PT", "Portuguese"),
    RUSSIAN("RU", "Russian");

    @Getter
    private final String isoCode;

    @Getter
    private final String label;

    Language(final String isoCode, final String label) {
        this.isoCode = isoCode;
        this.label = label;
    }
}
