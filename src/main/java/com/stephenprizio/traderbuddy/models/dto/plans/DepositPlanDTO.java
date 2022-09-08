package com.stephenprizio.traderbuddy.models.dto.plans;

import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.models.dto.GenericDTO;
import com.stephenprizio.traderbuddy.models.entities.plans.DepositPlan;
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
    private Double amount;

    @Getter
    @Setter
    private CompoundFrequency frequency;


    //  METHODS

    @Override
    public Boolean isEmpty() {
        return this.amount == null && this.frequency == null;
    }
}
