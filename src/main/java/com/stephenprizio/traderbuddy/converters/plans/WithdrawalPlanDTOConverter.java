package com.stephenprizio.traderbuddy.converters.plans;

import com.stephenprizio.traderbuddy.converters.GenericDTOConverter;
import com.stephenprizio.traderbuddy.models.dto.plans.WithdrawalPlanDTO;
import com.stephenprizio.traderbuddy.models.entities.plans.WithdrawalPlan;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converter for converting {@link WithdrawalPlan}s into {@link WithdrawalPlanDTO}s
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
@Component("withdrawalPlanDTOConverter")
public class WithdrawalPlanDTOConverter implements GenericDTOConverter<WithdrawalPlan, WithdrawalPlanDTO> {


    //  METHODS

    @Override
    public WithdrawalPlanDTO convert(WithdrawalPlan entity) {

        WithdrawalPlanDTO withdrawalPlanDTO = new WithdrawalPlanDTO();

        if (entity != null) {
            withdrawalPlanDTO.setAmount(entity.getAmount());
            withdrawalPlanDTO.setFrequency(entity.getFrequency());
        }

        return withdrawalPlanDTO;
    }

    @Override
    public List<WithdrawalPlanDTO> convertAll(List<WithdrawalPlan> entities) {
        return entities.stream().map(this::convert).toList();
    }
}
