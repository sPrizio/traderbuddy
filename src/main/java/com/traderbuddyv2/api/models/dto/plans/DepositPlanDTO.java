package com.traderbuddyv2.api.models.dto.plans;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.plan.DepositPlan;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representation of a {@link DepositPlan}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class DepositPlanDTO implements GenericDTO {

    @Getter
    @Setter
    public String uid;

    @Getter
    @Setter
    private double amount;

    @Getter
    @Setter
    private boolean absolute;

    @Getter
    @Setter
    private AggregateInterval aggregateInterval;
}
