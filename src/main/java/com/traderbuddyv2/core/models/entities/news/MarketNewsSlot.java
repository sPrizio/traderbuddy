package com.traderbuddyv2.core.models.entities.news;

import com.traderbuddyv2.core.models.entities.GenericEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representation of a market news entry which represents a time of day that can have 1 or more pieces of news
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "market_news_slots")
public class MarketNewsSlot implements GenericEntity, Comparable<MarketNewsSlot> {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column
    private LocalTime time;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MarketNews news;

    @Getter
    @Setter
    @OneToMany(mappedBy = "slot", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("severity DESC")
    private List<MarketNewsEntry> entries;


    //  METHODS

    /**
     * Database assistance method
     *
     * @param entry {@link MarketNewsEntry}
     */
    public void addEntry(MarketNewsEntry entry) {

        if (getEntries() == null) {
            this.entries = new ArrayList<>();
        }

        getEntries().add(entry);
        entry.setSlot(this);
    }

    /**
     * Database assistance method
     *
     * @param entry {@link MarketNewsEntry}
     */
    public void removeEntry(MarketNewsEntry entry) {
        if (getEntries() != null) {
            List<MarketNewsEntry> entries = new ArrayList<>(getEntries());
            entries.remove(entry);
            this.entries = entries;
            entry.setSlot(null);
        }
    }

    @Override
    public int compareTo(MarketNewsSlot o) {
        return this.time.compareTo(o.time);
    }
}
