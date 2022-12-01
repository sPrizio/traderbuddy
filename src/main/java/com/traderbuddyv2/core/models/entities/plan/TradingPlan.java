package com.traderbuddyv2.core.models.entities.plan;

import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.enums.plans.TradingPlanStatus;
import com.traderbuddyv2.core.models.entities.GenericEntity;
import com.traderbuddyv2.core.models.entities.account.Account;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

/**
 * Class representation of a trading plan, a plan to help forecast execution, deposits and withdrawal
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
    private double profitTarget;

    @Getter
    @Setter
    @Column
    private boolean absolute;

    @Getter
    @Setter
    @Column
    private boolean active;

    @Getter
    @Setter
    @Column
    private TradingPlanStatus status;

    @Getter
    @Setter
    @Column
    private AggregateInterval aggregateInterval;

    @Getter
    @Setter
    @Column
    private double startingBalance;

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
    @OneToMany(mappedBy = "tradingPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("start ASC")
    private List<ExcludedPeriod> excludedPeriods;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Account account;
}
