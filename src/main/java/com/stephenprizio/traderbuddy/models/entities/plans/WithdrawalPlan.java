package com.stephenprizio.traderbuddy.models.entities.plans;

import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.models.entities.GenericEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Class representation of a plan to withdraw from the balance of a {@link TradingPlan}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "withdrawal_plans")
public class WithdrawalPlan implements GenericEntity {

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
