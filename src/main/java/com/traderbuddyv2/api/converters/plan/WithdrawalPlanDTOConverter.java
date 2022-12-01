package com.traderbuddyv2.api.converters.plan;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.models.dto.plans.WithdrawalPlanDTO;
import com.traderbuddyv2.core.models.entities.plan.WithdrawalPlan;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

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
            withdrawalPlanDTO.setAbsolute(entity.isAbsolute());
            withdrawalPlanDTO.setAggregateInterval(entity.getAggregateInterval());
        }

        return withdrawalPlanDTO;
    }
}
