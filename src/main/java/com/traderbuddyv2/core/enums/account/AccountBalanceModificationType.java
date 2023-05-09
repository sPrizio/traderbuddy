package com.traderbuddyv2.core.enums.account;

import com.traderbuddyv2.core.enums.system.Parity;
import lombok.Getter;

/**
 * Enum of a types of account balance modifications
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum AccountBalanceModificationType {
    ONE_TIME_DEPOSIT("One-time Deposit", Parity.POSITIVE),
    ONE_TIME_WITHDRAWAL("One-time Withdrawal", Parity.NEGATIVE),
    RECURRING_DEPOSIT("Recurring Deposit", Parity.POSITIVE),
    RECURRING_WITHDRAWAL("Recurring Withdrawal", Parity.NEGATIVE);

    @Getter
    private final String description;

    @Getter
    private final Parity parity;

    AccountBalanceModificationType(final String description, final Parity parity) {
        this.description = description;
        this.parity = parity;
    }

    /**
     * Returns a {@link AccountBalanceModificationType} for the given ordinal
     *
     * @param ordinal enum ordinal
     * @return {@link AccountBalanceModificationType}
     */
    public static AccountBalanceModificationType getForOrdinal(final int ordinal) {
        return switch (ordinal) {
            case 0 -> ONE_TIME_DEPOSIT;
            case 1 -> ONE_TIME_WITHDRAWAL;
            case 2 -> RECURRING_DEPOSIT;
            case 3 -> RECURRING_WITHDRAWAL;
            default -> null;
        };
    }
}
