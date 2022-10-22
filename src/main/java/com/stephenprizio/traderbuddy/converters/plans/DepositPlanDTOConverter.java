package com.stephenprizio.traderbuddy.converters.plans;

import com.stephenprizio.traderbuddy.converters.GenericDTOConverter;
import com.stephenprizio.traderbuddy.models.dto.plans.DepositPlanDTO;
import com.stephenprizio.traderbuddy.models.entities.plans.DepositPlan;
import com.stephenprizio.traderbuddy.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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
            depositPlanDTO.setFrequency(entity.getFrequency());
        }

        return depositPlanDTO;
    }
}
