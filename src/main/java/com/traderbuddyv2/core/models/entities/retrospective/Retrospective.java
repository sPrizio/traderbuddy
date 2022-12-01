package com.traderbuddyv2.core.models.entities.retrospective;

import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.GenericEntity;
import com.traderbuddyv2.core.models.entities.account.Account;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Class representation of a trading performance retrospective for a given time span
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "retrospectives", uniqueConstraints = @UniqueConstraint(name = "UniqueIntervalAndStartDateAndEndDate", columnNames = {"interval_frequency", "start_date", "end_date"}))
public class Retrospective implements GenericEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "start_date")
    private LocalDate startDate;

    @Getter
    @Setter
    @Column(name = "end_date")
    private LocalDate endDate;

    @Getter
    @Setter
    @Column(name = "interval_frequency")
    private AggregateInterval intervalFrequency;

    @Getter
    @Setter
    @OneToMany(mappedBy = "retrospective", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("lineNumber ASC")
    private List<RetrospectiveEntry> points = new ArrayList<>();

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Account account;


    //  METHODS

    /**
     * Database assistance method
     *
     * @param entry {@link RetrospectiveEntry}
     */
    public void addPoint(RetrospectiveEntry entry) {

        if (getPoints() == null) {
            this.points = new ArrayList<>();
        }

        getPoints().add(entry);
        entry.setRetrospective(this);
    }

    /**
     * Database assistance method
     *
     * @param entry {@link RetrospectiveEntry}
     */
    public void removePoint(RetrospectiveEntry entry) {
        if (getPoints() != null) {
            List<RetrospectiveEntry> entries = new ArrayList<>(getPoints());
            entries.remove(entry);
            this.points = entries;
            entry.setRetrospective(null);
        }
    }
}
