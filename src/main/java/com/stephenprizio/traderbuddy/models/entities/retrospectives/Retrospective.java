package com.stephenprizio.traderbuddy.models.entities.retrospectives;

import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Class representation of a trading performance retrospective for a given time span
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "retrospectives", uniqueConstraints = @UniqueConstraint(name = "UniqueIntervalAndStartDateAndEndDate", columnNames = {"interval_frequency", "start_date", "end_date"}))
public class Retrospective {

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
    @OneToMany(mappedBy = "retrospective", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("lineNumber ASC")
    private List<RetrospectiveEntry> points;


    //  METHODS

    /**
     * Database assistance method
     *
     * @param entry {@link RetrospectiveEntry}
     */
    public void addPoint(RetrospectiveEntry entry) {
        this.points.add(entry);
        entry.setRetrospective(this);
    }

    /**
     * Database assistance method
     *
     * @param entry {@link RetrospectiveEntry}
     */
    public void removePoint(RetrospectiveEntry entry) {
        this.points.remove(entry);
        entry.setRetrospective(null);
    }
}
