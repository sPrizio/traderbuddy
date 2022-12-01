package com.traderbuddyv2.core.models.entities.plan;

import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.GenericEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Class representation of an excluded period for a {@link TradingPlan}. The idea for this is to allow more flexibility for
 * a plan to better take into account period where trading or account growth is impossible such as : holidays where the market would be
 * closed, a week off for a vacation or perhaps months off for personal leave
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "excluded_periods")
public class ExcludedPeriod implements GenericEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column
    private LocalDate start;

    @Getter
    @Setter
    @Column
    private LocalDate end;

    @Getter
    @Setter
    @Column
    private AggregateInterval aggregateInterval;

    @Getter
    @Setter
    @Lob
    @Column
    private String reasonForExclusion;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private TradingPlan tradingPlan;
}
