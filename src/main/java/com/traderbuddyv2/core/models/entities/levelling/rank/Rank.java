package com.traderbuddyv2.core.models.entities.levelling.rank;

import com.traderbuddyv2.core.models.entities.GenericEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Class representation of a rank, a {@link Rank} is a way of scoring a user's account based on it's value in relation to its starting value as a means of tracking progress and evolution
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "ranks")
public class Rank implements GenericEntity, Comparable<Rank> {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private BaseRank baseRank;

    @Getter
    @Setter
    @Column
    private int level;

    @Getter
    @Setter
    @Column
    private String imageUrl;


    //  METHODS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rank rank = (Rank) o;
        return id.equals(rank.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(Rank o) {
        return Integer.compare(this.level, o.level);
    }
}
