package com.traderbuddyv2.api.models.dto.levelling.rank;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.core.models.entities.levelling.rank.BaseRank;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

/**
 * A DTO representation of a {@link BaseRank}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class BaseRankDTO implements GenericDTO, Comparable<BaseRankDTO> {

    @Getter
    @Setter
    private String uid;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private int multiplier;

    @Getter
    @Setter
    private int priority;

    @Getter
    @Setter
    private Set<RankDTO> ranks;


    //  METHODS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseRankDTO that = (BaseRankDTO) o;
        return uid.equals(that.uid);
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }

    @Override
    public int compareTo(BaseRankDTO o) {
        return Integer.compare(this.priority, o.priority);
    }
}
