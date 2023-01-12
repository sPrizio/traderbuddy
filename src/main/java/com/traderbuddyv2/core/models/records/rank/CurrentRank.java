package com.traderbuddyv2.core.models.records.rank;

import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.levelling.rank.Rank;
import lombok.Getter;

/**
 * Class representation of an {@link Account}'s current {@link Rank} meant to show current position within the ranking system
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record CurrentRank(
        @Getter
        String name,
        @Getter
        int start,
        @Getter
        int current,
        @Getter
        int end,
        @Getter
        String imageUrl
) {}
