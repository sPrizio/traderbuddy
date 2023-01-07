package com.traderbuddyv2.core.models.entities.levelling.rank;

import com.traderbuddyv2.core.models.entities.GenericEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * A base rank represents the categories that a {@link Rank} might fall in to. An example would that a Gold Rank can have 5 sub-ranks
 * Gold 1, Gold 2 ...
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "base_ranks")
public class BaseRank implements GenericEntity, Comparable<BaseRank> {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column
    private String name;

    @Getter
    @Setter
    @Column
    private int multiplier;

    @Getter
    @Setter
    @Column
    private int priority;

    @Getter
    @Setter
    @OneToMany(mappedBy = "baseRank", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("level DESC")
    private List<Rank> ranks;


    //  METHODS

    /**
     * Database assistance method
     *
     * @param rank {@link Rank}
     */
    public void addRank(Rank rank) {

        if (getRanks() == null) {
            this.ranks = new ArrayList<>();
        }

        getRanks().add(rank);
        rank.setBaseRank(this);
    }

    /**
     * Database assistance method
     *
     * @param entry {@link Rank}
     */
    public void removeRank(Rank entry) {
        if (getRanks() != null) {
            List<Rank> entries = new ArrayList<>(getRanks());
            entries.remove(entry);
            this.ranks = entries;
            entry.setBaseRank(null);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BaseRank baseRank = (BaseRank) o;
        return id.equals(baseRank.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public int compareTo(BaseRank o) {
        return Integer.compare(this.priority, o.priority);
    }
}
