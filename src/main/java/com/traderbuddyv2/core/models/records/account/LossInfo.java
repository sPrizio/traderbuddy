package com.traderbuddyv2.core.models.records.account;

import com.traderbuddyv2.core.enums.account.StopLimitType;
import com.traderbuddyv2.core.models.entities.account.Account;
import lombok.Getter;

/**
 * Contains loss information for an {@link Account}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record LossInfo(
        @Getter
        StopLimitType type,
        @Getter
        double limit,
        @Getter
        double excess,
        @Getter
        double adjusted,
        @Getter
        int occurrences
) {}
