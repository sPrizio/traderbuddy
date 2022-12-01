package com.traderbuddyv2.core.models.entities.plan;

import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.GenericEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Class representation of a deposit plan, a periodic credit into an overall trading plan
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "deposit_plans")
public class DepositPlan implements GenericEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column
    private double amount;

    @Getter
    @Setter
    @Column
    private boolean absolute;

    @Getter
    @Setter
    @Column
    private AggregateInterval aggregateInterval;
}
