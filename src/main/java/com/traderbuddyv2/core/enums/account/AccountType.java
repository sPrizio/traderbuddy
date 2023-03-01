package com.traderbuddyv2.core.enums.account;

import lombok.Getter;

/**
 * Enum representing the type of account. Here type of account represents the type of securities being traded
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum AccountType {
    SHARES("Shares"),
    OPTIONS("Options"),
    CFD("CFD"),
    FOREX("Forex"),
    DEMO("Demo");

    @Getter
    private final String label;

    AccountType(final String label) {
        this.label = label;
    }
}
