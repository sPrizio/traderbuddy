package com.traderbuddyv2.api.models.dto.levelling.rank;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.core.models.entities.levelling.rank.Rank;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representation of a {@link Rank}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class RankDTO implements GenericDTO, Comparable<RankDTO> {

    @Getter
    @Setter
    private String uid;

    @Getter
    @Setter
    private int level;

    @Getter
    @Setter
    private String imageUrl;


    //  METHODS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        RankDTO rankDTO = (RankDTO) o;

        return uid.equals(rankDTO.uid);
    }

    @Override
    public int hashCode() {
        return uid.hashCode();
    }

    @Override
    public int compareTo(RankDTO o) {
        return Integer.compare(this.level, o.level);
    }
}
