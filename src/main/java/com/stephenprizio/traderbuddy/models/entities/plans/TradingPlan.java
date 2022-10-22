package com.stephenprizio.traderbuddy.models.entities.plans;

import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.enums.plans.TradingPlanStatus;
import com.stephenprizio.traderbuddy.models.entities.GenericEntity;
import com.stephenprizio.traderbuddy.models.entities.account.Account;
import com.stephenprizio.traderbuddy.models.entities.trades.Trade;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingRecord;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

/**
 * Class representation of a trading plan. A {@link TradingPlan} will be used to assess the effectiveness of individual {@link Trade}s as part of a {@link TradingRecord}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "trading_plans", uniqueConstraints = @UniqueConstraint(name = "UniqueNameAndStartDateAndEndDate", columnNames = {"name", "start_date", "end_date"}))
public class TradingPlan implements GenericEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "name")
    private String name;

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
    @Column
    private Double profitTarget;

    @Getter
    @Setter
    @Column
    private Boolean active;

    @Getter
    @Setter
    @Column
    private TradingPlanStatus status;

    @Getter
    @Setter
    @Column
    private CompoundFrequency compoundFrequency;

    @Getter
    @Setter
    @Column
    private Double startingBalance;

    @Getter
    @Setter
    @OneToOne
    private DepositPlan depositPlan;

    @Getter
    @Setter
    @OneToOne
    private WithdrawalPlan withdrawalPlan;

    @Getter
    @Setter
    @OneToOne(mappedBy = "tradingPlan")
    private Account account;
}
