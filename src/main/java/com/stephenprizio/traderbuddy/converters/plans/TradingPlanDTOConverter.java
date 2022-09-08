package com.stephenprizio.traderbuddy.converters.plans;

import com.stephenprizio.traderbuddy.converters.GenericDTOConverter;
import com.stephenprizio.traderbuddy.models.dto.plans.TradingPlanDTO;
import com.stephenprizio.traderbuddy.models.entities.plans.TradingPlan;
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

    @Resource(name = "withdrawalPlanDTOConverter")
    private WithdrawalPlanDTOConverter withdrawalPlanDTOConverter;


    //  METHODS

    @Override
    public TradingPlanDTO convert(TradingPlan entity) {

        if (entity == null) {
            return new TradingPlanDTO();
        }

        TradingPlanDTO tradingPlanDTO = new TradingPlanDTO();

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

    @Override
    public List<TradingPlanDTO> convertAll(List<TradingPlan> entities) {
        return entities.stream().map(this::convert).toList();
    }
}
