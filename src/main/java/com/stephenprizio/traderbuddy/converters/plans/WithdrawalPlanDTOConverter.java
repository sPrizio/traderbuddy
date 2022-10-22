package com.stephenprizio.traderbuddy.converters.plans;

import com.stephenprizio.traderbuddy.converters.GenericDTOConverter;
import com.stephenprizio.traderbuddy.models.dto.plans.WithdrawalPlanDTO;
import com.stephenprizio.traderbuddy.models.entities.plans.WithdrawalPlan;
import com.stephenprizio.traderbuddy.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * Converter for converting {@link WithdrawalPlan}s into {@link WithdrawalPlanDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("withdrawalPlanDTOConverter")
public class WithdrawalPlanDTOConverter implements GenericDTOConverter<WithdrawalPlan, WithdrawalPlanDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public WithdrawalPlanDTO convert(final WithdrawalPlan entity) {

        WithdrawalPlanDTO withdrawalPlanDTO = new WithdrawalPlanDTO();

        if (entity != null) {
            withdrawalPlanDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
            withdrawalPlanDTO.setAmount(entity.getAmount());
            withdrawalPlanDTO.setFrequency(entity.getFrequency());
        }

        return withdrawalPlanDTO;
    }
}
