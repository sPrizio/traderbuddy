package com.traderbuddyv2.api.converters.plan;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.models.dto.plans.DepositPlanDTO;
import com.traderbuddyv2.core.models.entities.plan.DepositPlan;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Convert that converts {@link DepositPlan}s into {@link DepositPlanDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("depositPlanDTOConverter")
public class DepositPlanDTOConverter implements GenericDTOConverter<DepositPlan, DepositPlanDTO> {

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    @Override
    public DepositPlanDTO convert(final DepositPlan entity) {

        DepositPlanDTO depositPlanDTO = new DepositPlanDTO();

        if (entity != null) {
            depositPlanDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
            depositPlanDTO.setAmount(entity.getAmount());
            depositPlanDTO.setAbsolute(entity.isAbsolute());
            depositPlanDTO.setAggregateInterval(entity.getAggregateInterval());
        }

        return depositPlanDTO;
    }
}
