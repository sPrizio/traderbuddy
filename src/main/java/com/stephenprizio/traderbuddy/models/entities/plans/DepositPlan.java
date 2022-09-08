package com.stephenprizio.traderbuddy.models.entities.plans;

import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Class representation of a deposit plan to update the balance within a {@link TradingPlan}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "deposit_plans")
public class DepositPlan {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column
    private Double amount;

    @Getter
    @Setter
    @Column
    private CompoundFrequency frequency;
}
