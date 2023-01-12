package com.traderbuddyv2.core.models.records.rank;

import lombok.Getter;

/**
 * Class representation of a rank interval, representing delineations of sub ranks
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record RankInterval(
        @Getter
        Long uid,
        @Getter
        String name,
        @Getter
        int value,
        @Getter
        int increment,
        @Getter
        String imageUrl,
        @Getter
        String className
) {}
