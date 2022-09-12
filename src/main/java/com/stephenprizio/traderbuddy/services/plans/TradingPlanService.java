package com.stephenprizio.traderbuddy.services.plans;

import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.enums.plans.TradingPlanStatus;
import com.stephenprizio.traderbuddy.exceptions.system.EntityCreationException;
import com.stephenprizio.traderbuddy.exceptions.system.EntityModificationException;
import com.stephenprizio.traderbuddy.exceptions.validation.MissingRequiredDataException;
import com.stephenprizio.traderbuddy.exceptions.validation.NoResultFoundException;
import com.stephenprizio.traderbuddy.models.entities.plans.DepositPlan;
import com.stephenprizio.traderbuddy.models.entities.plans.TradingPlan;
import com.stephenprizio.traderbuddy.models.entities.plans.WithdrawalPlan;
import com.stephenprizio.traderbuddy.repositories.plans.DepositPlanRepository;
import com.stephenprizio.traderbuddy.repositories.plans.TradingPlanRepository;
import com.stephenprizio.traderbuddy.repositories.plans.WithdrawalPlanRepository;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.*;

/**
 * Service-layer for {@link TradingPlan}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("tradingPlanService")
public class TradingPlanService {

    @Resource(name = "depositPlanRepository")
    private DepositPlanRepository depositPlanRepository;

    @Resource(name = "tradingPlanRepository")
    private TradingPlanRepository tradingPlanRepository;

    @Resource(name = "withdrawalPlanRepository")
    private WithdrawalPlanRepository withdrawalPlanRepository;


    //  METHODS

    /**
     * Returns an {@link Optional} {@link TradingPlan} that is currently active. Note that the system only supports 1 active tradingPlan at a time
     *
     * @return {@link Optional} {@link TradingPlan}
     */
    public Optional<TradingPlan> findCurrentlyActiveTradingPlan() {
        List<TradingPlan> tradingPlans = this.tradingPlanRepository.findTradingPlanByActiveIsTrue();

        validateIfAnyResult(tradingPlans, "No active trading plans were found");
        validateIfSingleResult(tradingPlans, "One or more active tradingPlans was found. This is not allowed. Only 1 tradingPlan can be active");

        return Optional.of(tradingPlans.get(0));
    }

    /**
     * Returns a {@link List} of {@link TradingPlan}s by their {@link TradingPlanStatus}
     *
     * @param status {@link TradingPlanStatus}
     * @return {@link List} of {@link TradingPlan}s
     */
    public List<TradingPlan> findTradingPlansForStatus(TradingPlanStatus status) {
        validateParameterIsNotNull(status, "tradingPlan status cannot be null");
        return this.tradingPlanRepository.findAllByStatus(status);
    }

    /**
     * Returns an {@link Optional} {@link TradingPlan} for the given name, start and end date
     *
     * @param name tradingPlan name
     * @param startDate {@link LocalDate} start date
     * @param endDate {@link LocalDate} end date
     * @return {@link Optional} {@link TradingPlan}
     */
    public Optional<TradingPlan> findTradingPlanForNameAndStartDateAndEndDate(String name, LocalDate startDate, LocalDate endDate) {

        validateParameterIsNotNull(name, "name cannot be null");
        validateParameterIsNotNull(startDate, "startDate cannot be null");
        validateParameterIsNotNull(endDate, "endDate cannot be null");
        validateDatesAreNotMutuallyExclusive(startDate.atStartOfDay(), endDate.atStartOfDay(), "startDate was after endDate or vice versa");

        return Optional.ofNullable(this.tradingPlanRepository.findTradingPlanByNameAndStartDateAndEndDate(name, startDate, endDate));
    }

    /**
     * Creates a new {@link TradingPlan} from the given {@link Map} of data
     *
     * @param data {@link Map}
     * @return newly created {@link TradingPlan}
     */
    public TradingPlan createTradingPlan(Map<String, Object> data) {

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for creating a TradingPlan was null or empty");
        }

        try {
            TradingPlan tradingPlan = this.tradingPlanRepository.save(applyChanges(new TradingPlan(), data));

            //  set all other tradingPlans as inactive
            if (Boolean.TRUE.equals(tradingPlan.getActive())) {
                this.tradingPlanRepository.resetTradingPlans();
            }

            return tradingPlan;
        } catch (Exception e) {
            throw new EntityCreationException(String.format("A TradingPlan entity could not be created : %s" , e.getMessage()));
        }
    }

    /**
     * Updates an existing {@link TradingPlan} with the given {@link Map} of data. Update methods are designed to be idempotent.
     *
     * @param data {@link Map}
     * @return modified {@link TradingPlan}
     */
    public TradingPlan updateTradingPlan(String name, LocalDate start, LocalDate end, Map<String, Object> data) {

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for updating the TradingPlan was null or empty");
        }

        try {
            TradingPlan tradingPlan =
                    findTradingPlanForNameAndStartDateAndEndDate(name, start, end)
                            .orElseThrow(() -> new NoResultFoundException(String.format("No TradingPlan found for name %s , start date %s and end date %s", name, start.format(DateTimeFormatter.ISO_DATE), end.format(DateTimeFormatter.ISO_DATE))));

            return applyChanges(tradingPlan, data);
        } catch (Exception e) {
            throw new EntityModificationException(String.format("An error occurred while modifying the TradingPlan : %s" , e.getMessage()));
        }
    }


    //  HELPERS

    /**
     * Applies data changes to the given {@link TradingPlan}
     *
     * @param tradingPlan {@link TradingPlan} to change
     * @param data {@link Map} of data
     * @return {@link TradingPlan}
     */
    private TradingPlan applyChanges(final TradingPlan tradingPlan, Map<String, Object> data) {

        Map<String, Object> plan = (Map<String, Object>) data.get("plan");

        tradingPlan.setStatus(TradingPlanStatus.valueOf(plan.get("status").toString().toUpperCase()));
        tradingPlan.setActive(Boolean.parseBoolean(plan.get("active").toString()));
        tradingPlan.setName(plan.get("name").toString());
        tradingPlan.setStartDate(LocalDate.parse(plan.get("startDate").toString(), DateTimeFormatter.ISO_DATE));
        tradingPlan.setEndDate(LocalDate.parse(plan.get("endDate").toString(), DateTimeFormatter.ISO_DATE));
        tradingPlan.setProfitTarget(BigDecimal.valueOf(Double.parseDouble(plan.get("profitTarget").toString())).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        tradingPlan.setCompoundFrequency(CompoundFrequency.valueOf(plan.get("compoundFrequency").toString()));
        tradingPlan.setStartingBalance(BigDecimal.valueOf(Double.parseDouble(plan.get("startingBalance").toString())).setScale(2, RoundingMode.HALF_EVEN).doubleValue());

        if (data.containsKey("depositPlan")) {
            DepositPlan depositPlan = new DepositPlan();
            Map<String, Object> depPlan = (Map<String, Object>) data.get("depositPlan");

            depositPlan.setAmount(BigDecimal.valueOf(Double.parseDouble(depPlan.get("amount").toString())).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
            depositPlan.setFrequency(CompoundFrequency.valueOf(depPlan.get("frequency").toString().toUpperCase()));

            depositPlan = this.depositPlanRepository.save(depositPlan);
            tradingPlan.setDepositPlan(depositPlan);
        }

        if (data.containsKey("withdrawalPlan")) {
            WithdrawalPlan withdrawalPlan = new WithdrawalPlan();
            Map<String, Object> witPlan = (Map<String, Object>) data.get("withdrawalPlan");

            withdrawalPlan.setAmount(BigDecimal.valueOf(Double.parseDouble(witPlan.get("amount").toString())).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
            withdrawalPlan.setFrequency(CompoundFrequency.valueOf(witPlan.get("frequency").toString().toUpperCase()));

            withdrawalPlan = this.withdrawalPlanRepository.save(withdrawalPlan);
            tradingPlan.setWithdrawalPlan(withdrawalPlan);
        }

        return this.tradingPlanRepository.save(tradingPlan);
    }
}
