package com.traderbuddyv2.core.models.entities.news;

import com.traderbuddyv2.core.enums.news.MarketNewsSeverity;
import com.traderbuddyv2.core.models.entities.GenericEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Class representation of market news entry, a piece of news at a specific time
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "market_news_entries")
public class MarketNewsEntry implements GenericEntity, Comparable<MarketNewsEntry> {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column
    private String content;

    @Getter
    @Setter
    @Column
    private MarketNewsSeverity severity;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private MarketNewsSlot slot;


    //  METHODS

    @Override
    public int compareTo(MarketNewsEntry o) {
        return this.content.compareTo(o.content);
    }
}
