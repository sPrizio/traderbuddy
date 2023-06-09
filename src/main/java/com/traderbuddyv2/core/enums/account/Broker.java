package com.traderbuddyv2.core.enums.account;

import lombok.Getter;

/**
 * Enum representing the different brokers supported
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum Broker {
    CMC_MARKETS("CMC Markets"),
    FTMO("FTMO"),
    NA("Demo");

    @Getter
    private final String name;

    Broker(final String name) {
        this.name = name;
    }
}
