package com.traderbuddyv2.api.models.records;

import com.traderbuddyv2.api.models.dto.account.AccountDTO;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.records.rank.CurrentRank;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Class representation of an {@link Account}'s overview
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record AccountOverview(
        @Getter
        LocalDateTime dateTime,
        @Getter
        double balance,
        @Getter
        double monthlyEarnings,
        @Getter
        double dailyEarnings,
        @Getter
        double nextTarget,
        @Getter
        AccountDTO account,
        @Getter
        CurrentRank rank
) { }
