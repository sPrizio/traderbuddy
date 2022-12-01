package com.traderbuddyv2.api.converters.plan;

import com.traderbuddyv2.api.converters.GenericDTOConverter;
import com.traderbuddyv2.api.models.dto.plans.TradingPlanDTO;
import com.traderbuddyv2.core.models.entities.plan.TradingPlan;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Converter for {@link TradingPlan}s into {@link TradingPlanDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("tradingPlanDTOConverter")
public class TradingPlanDTOConverter implements GenericDTOConverter<TradingPlan, TradingPlanDTO> {

    @Resource(name = "depositPlanDTOConverter")
    private DepositPlanDTOConverter depositPlanDTOConverter;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;

    @Resource(name = "withdrawalPlanDTOConverter")
    private WithdrawalPlanDTOConverter withdrawalPlanDTOConverter;


    //  METHODS

    @Override
    public TradingPlanDTO convert(final TradingPlan entity) {

        if (entity == null) {
            return new TradingPlanDTO();
        }

        TradingPlanDTO tradingPlanDTO = new TradingPlanDTO();

        tradingPlanDTO.setUid(this.uniqueIdentifierService.generateUid(entity));
        tradingPlanDTO.setActive(entity.isActive());
        tradingPlanDTO.setName(entity.getName());
        tradingPlanDTO.setStartDate(entity.getStartDate());
        tradingPlanDTO.setEndDate(entity.getEndDate());
        tradingPlanDTO.setProfitTarget(entity.getProfitTarget());
        tradingPlanDTO.setAbsolute(entity.isAbsolute());
        tradingPlanDTO.setStatus(entity.getStatus());
        tradingPlanDTO.setAggregateInterval(entity.getAggregateInterval());
        tradingPlanDTO.setStartingBalance(entity.getStartingBalance());
        tradingPlanDTO.setDepositPlan(this.depositPlanDTOConverter.convert(entity.getDepositPlan()));
        tradingPlanDTO.setWithdrawalPlan(this.withdrawalPlanDTOConverter.convert(entity.getWithdrawalPlan()));

        return tradingPlanDTO;
    }
}
