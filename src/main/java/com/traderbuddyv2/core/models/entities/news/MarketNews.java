package com.traderbuddyv2.core.models.entities.news;

import com.traderbuddyv2.core.models.entities.GenericEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representation of market news on a specific day
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "market_news")
public class MarketNews implements GenericEntity, Comparable<MarketNews> {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column
    private LocalDate date;

    @Getter
    @Setter
    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("time ASC")
    private List<MarketNewsSlot> slots;


    //  METHODS

    /**
     * Database assistance method
     *
     * @param slot {@link MarketNewsSlot}
     */
    public void addSlot(MarketNewsSlot slot) {

        if (getSlots() == null) {
            this.slots = new ArrayList<>();
        }

        getSlots().add(slot);
        slot.setNews(this);
    }

    /**
     * Database assistance method
     *
     * @param slot {@link MarketNewsSlot}
     */
    public void removeSlot(MarketNewsSlot slot) {
        if (getSlots() != null) {
            List<MarketNewsSlot> entries = new ArrayList<>(getSlots());
            entries.remove(slot);
            this.slots = entries;
            slot.setNews(null);
        }
    }

    @Override
    public int compareTo(MarketNews o) {
        return this.date.compareTo(o.date);
    }
}
