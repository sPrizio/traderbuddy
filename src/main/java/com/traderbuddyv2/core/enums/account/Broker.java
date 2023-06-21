package com.traderbuddyv2.core.enums.account;

import lombok.Getter;

/**
 * Enum representing the different brokers supported
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum Broker {
    CMC_MARKETS("CMC_MARKETS", "CMC Markets"),
    FTMO("FTMO", "FTMO"),
    NA("N/A", "Demo");

    @Getter
    private final String code;

    @Getter
    private final String name;

    Broker(final String code, final String name) {
        this.code = code;
        this.name = name;
    }
}
