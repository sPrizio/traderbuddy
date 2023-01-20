package com.traderbuddyv2.core.enums.account;

import lombok.Getter;

/**
 * Enum of a types of account balance modifications
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum AccountBalanceModificationType {
    ONE_TIME_DEPOSIT("One-time Deposit"),
    ONE_TIME_WITHDRAWAL("One-time Withdrawal"),
    RECURRING_DEPOSIT("Recurring Deposit"),
    RECURRING_WITHDRAWAL("Recurring Withdrawal");

    @Getter
    private final String description;

    AccountBalanceModificationType(final String description) {
        this.description = description;
    }
}
