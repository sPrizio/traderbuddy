package com.stephenprizio.traderbuddy.converters.plans;

import com.stephenprizio.traderbuddy.converters.GenericDTOConverter;
import com.stephenprizio.traderbuddy.models.dto.plans.TradingPlanDTO;
import com.stephenprizio.traderbuddy.models.entities.plans.TradingPlan;
import com.stephenprizio.traderbuddy.services.platform.UniqueIdentifierService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

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
        tradingPlanDTO.setActive(entity.getActive());
        tradingPlanDTO.setName(entity.getName());
        tradingPlanDTO.setStartDate(entity.getStartDate());
        tradingPlanDTO.setEndDate(entity.getEndDate());
        tradingPlanDTO.setProfitTarget(entity.getProfitTarget());
        tradingPlanDTO.setStatus(entity.getStatus());
        tradingPlanDTO.setCompoundFrequency(entity.getCompoundFrequency());
        tradingPlanDTO.setStartingBalance(entity.getStartingBalance());
        tradingPlanDTO.setDepositPlan(this.depositPlanDTOConverter.convert(entity.getDepositPlan()));
        tradingPlanDTO.setWithdrawalPlan(this.withdrawalPlanDTOConverter.convert(entity.getWithdrawalPlan()));

        return tradingPlanDTO;
    }
}
