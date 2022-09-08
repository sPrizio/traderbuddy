package com.stephenprizio.traderbuddy.converters.plans;

import com.stephenprizio.traderbuddy.converters.GenericDTOConverter;
import com.stephenprizio.traderbuddy.models.dto.plans.DepositPlanDTO;
import com.stephenprizio.traderbuddy.models.entities.plans.DepositPlan;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Convert that converts {@link DepositPlan}s into {@link DepositPlanDTO}s
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
@Component("depositPlanDTOConverter")
public class DepositPlanDTOConverter implements GenericDTOConverter<DepositPlan, DepositPlanDTO> {


    //  METHODS

    @Override
    public DepositPlanDTO convert(DepositPlan entity) {

        DepositPlanDTO depositPlanDTO = new DepositPlanDTO();

        if (entity != null) {
            depositPlanDTO.setAmount(entity.getAmount());
            depositPlanDTO.setFrequency(entity.getFrequency());
        }

        return depositPlanDTO;
    }

    @Override
    public List<DepositPlanDTO> convertAll(List<DepositPlan> entities) {
        return entities.stream().map(this::convert).toList();
    }
}
