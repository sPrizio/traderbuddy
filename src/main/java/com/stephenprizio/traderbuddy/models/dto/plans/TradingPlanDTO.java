package com.stephenprizio.traderbuddy.models.dto.plans;

import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.enums.plans.TradingPlanStatus;
import com.stephenprizio.traderbuddy.models.dto.GenericDTO;
import com.stephenprizio.traderbuddy.models.entities.plans.TradingPlan;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

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
    private Double profitTarget;

    @Getter
    @Setter
    private Boolean active;

    @Getter
    @Setter
    private TradingPlanStatus status;

    @Getter
    @Setter
    private CompoundFrequency compoundFrequency;

    @Getter
    @Setter
    private Double startingBalance;

    @Getter
    @Setter
    private DepositPlanDTO depositPlan;

    @Getter
    @Setter
    private WithdrawalPlanDTO withdrawalPlan;


    //  METHODS

    @Override
    public Boolean isEmpty() {
        return StringUtils.isEmpty(this.uid);
    }
}
