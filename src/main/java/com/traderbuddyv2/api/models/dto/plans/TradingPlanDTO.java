package com.traderbuddyv2.api.models.dto.plans;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.enums.plans.TradingPlanStatus;
import com.traderbuddyv2.core.models.entities.plan.TradingPlan;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * A DTO representation of a {@link TradingPlan}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradingPlanDTO implements GenericDTO {

    @Getter
    @Setter
    public String uid;

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private LocalDate startDate;

    @Getter
    @Setter
    private LocalDate endDate;

    @Getter
    @Setter
    private double profitTarget;

    @Getter
    @Setter
    private boolean absolute;

    @Getter
    @Setter
    private boolean active;

    @Getter
    @Setter
    private TradingPlanStatus status;

    @Getter
    @Setter
    private AggregateInterval aggregateInterval;

    @Getter
    @Setter
    private double startingBalance;

    @Getter
    @Setter
    private DepositPlanDTO depositPlan;

    @Getter
    @Setter
    private WithdrawalPlanDTO withdrawalPlan;
}
