package com.stephenprizio.traderbuddy.models.dto.plans;

import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.models.dto.GenericDTO;
import com.stephenprizio.traderbuddy.models.entities.plans.WithdrawalPlan;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representation of a {@link WithdrawalPlan}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class WithdrawalPlanDTO implements GenericDTO {

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
