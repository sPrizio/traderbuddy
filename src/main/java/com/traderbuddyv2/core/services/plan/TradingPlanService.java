package com.traderbuddyv2.core.services.plan;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.enums.plans.CompoundFrequency;
import com.traderbuddyv2.core.enums.plans.TradingPlanStatus;
import com.traderbuddyv2.core.exceptions.system.EntityCreationException;
import com.traderbuddyv2.core.exceptions.system.EntityModificationException;
import com.traderbuddyv2.core.exceptions.validation.MissingRequiredDataException;
import com.traderbuddyv2.core.exceptions.validation.NoResultFoundException;
import com.traderbuddyv2.core.models.entities.plan.DepositPlan;
import com.traderbuddyv2.core.models.entities.plan.TradingPlan;
import com.traderbuddyv2.core.models.entities.plan.WithdrawalPlan;
import com.traderbuddyv2.core.models.records.calculator.FinancingInfo;
import com.traderbuddyv2.core.models.records.plan.ForecastEntry;
import com.traderbuddyv2.core.repositories.plan.DepositPlanRepository;
import com.traderbuddyv2.core.repositories.plan.TradingPlanRepository;
import com.traderbuddyv2.core.repositories.plan.WithdrawalPlanRepository;
import com.traderbuddyv2.core.services.calculator.CompoundInterestCalculator;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.*;

import static com.traderbuddyv2.core.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link TradingPlan}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("tradingPlanService")
public class TradingPlanService {

    @Resource(name = "compoundInterestCalculator")
    private CompoundInterestCalculator compoundInterestCalculator;

    @Resource(name = "depositPlanRepository")
    private DepositPlanRepository depositPlanRepository;

    @Resource(name = "traderBuddyUserDetailsService")
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

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
        return Optional.ofNullable(this.tradingPlanRepository.findTopTradingPlanByActiveIsTrueAndAccountOrderByStartDateDesc(this.traderBuddyUserDetailsService.getCurrentUser().getAccount()));
    }

    /**
     * Returns a {@link List} of {@link TradingPlan}s by their {@link TradingPlanStatus}
     *
     * @param status {@link TradingPlanStatus}
     * @return {@link List} of {@link TradingPlan}s
     */
    public List<TradingPlan> findTradingPlansForStatus(final TradingPlanStatus status) {
        validateParameterIsNotNull(status, "tradingPlan status cannot be null");
        return this.tradingPlanRepository.findAllByStatusAndAccount(status, this.traderBuddyUserDetailsService.getCurrentUser().getAccount());
    }

    /**
     * Returns an {@link Optional} {@link TradingPlan} for the given name, start and end date
     *
     * @param name tradingPlan name
     * @param startDate {@link LocalDate} start date
     * @param endDate {@link LocalDate} end date
     * @return {@link Optional} {@link TradingPlan}
     */
    public Optional<TradingPlan> findTradingPlanForNameAndStartDateAndEndDate(final String name, final LocalDate startDate, final LocalDate endDate) {

        validateParameterIsNotNull(name, "name cannot be null");
        validateParameterIsNotNull(startDate, CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(endDate, CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(startDate.atStartOfDay(), endDate.atStartOfDay(), CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);

        return Optional.ofNullable(this.tradingPlanRepository.findTradingPlanByNameAndStartDateAndEndDateAndAccount(name, startDate, endDate, this.traderBuddyUserDetailsService.getCurrentUser().getAccount()));
    }

    /**
     * Generates a {@link List} of {@link ForecastEntry}
     *
     * @param tradingPlan {@link TradingPlan}
     * @param interval {@link AggregateInterval}
     * @param begin start date for aggregation
     * @param limit end date for aggregation
     * @return {@link List} of {@link ForecastEntry}
     */
    public List<ForecastEntry> forecast(final TradingPlan tradingPlan, final AggregateInterval interval, final LocalDate begin, final LocalDate limit) {

        validateParameterIsNotNull(tradingPlan, "trading plan cannot be null");

        if (!tradingPlan.isActive()) {
            return Collections.emptyList();
        }

        validateParameterIsNotNull(tradingPlan.getAggregateInterval(), "aggregate interval cannot be null");
        validateParameterIsNotNull(tradingPlan.getStartDate(), "trading plan start date cannot be null");
        validateParameterIsNotNull(tradingPlan.getEndDate(), "trading plan end date cannot be null");
        validateParameterIsNotNull(interval, CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);
        validateParameterIsNotNull(begin, "begin cannot be null");
        validateParameterIsNotNull(limit, "limit cannot be null");
        validateDatesAreNotMutuallyExclusive(begin.atStartOfDay(), limit.atStartOfDay(), "The start date was after the end date or vice versa");

        LocalDate startDate = computeStart(tradingPlan.getStartDate(), tradingPlan.getAggregateInterval());
        LocalDate endDate = tradingPlan.getEndDate();
        LocalDate compare = startDate;

        validateDatesAreNotMutuallyExclusive(startDate.atStartOfDay(), endDate.atStartOfDay(), "start date cannot be after end date or vice versa");

        List<ForecastEntry> entries = new ArrayList<>();
        BigDecimal accruedEarnings = BigDecimal.ZERO;
        BigDecimal balance = BigDecimal.valueOf(tradingPlan.getStartingBalance());

        int index = 0;
        while (compare.isBefore(endDate)) {

            balance = computeDepositBalance(tradingPlan, compare, balance, index);
            balance = computeWithdrawalBalance(tradingPlan, compare, balance, index);

            BigDecimal earnings = this.compoundInterestCalculator.computeInterest(new FinancingInfo(balance.setScale(2, RoundingMode.HALF_EVEN).doubleValue(), tradingPlan.getProfitTarget(), tradingPlan.getAggregateInterval(), 1));
            accruedEarnings = accruedEarnings.add(earnings);
            balance = balance.add(earnings);

            entries.add(
                    new ForecastEntry(
                            compare,
                            computeUnit(tradingPlan.getAggregateInterval()).equals(ChronoUnit.WEEKS) ? compare.plus(6L, ChronoUnit.DAYS) : compare.plus(1L, computeUnit(tradingPlan.getAggregateInterval())),
                            earnings.setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                            accruedEarnings.setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                            balance.setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                            computeDepositBalance(tradingPlan, compare, balance, index).equals(balance) ? 0.0 : tradingPlan.getDepositPlan().getAmount(),
                            computeWithdrawalBalance(tradingPlan, compare, balance, index).equals(balance) ? 0.0 : tradingPlan.getWithdrawalPlan().getAmount(),
                            tradingPlan.getProfitTarget()
                    )
            );

            compare = compare.plus(1, computeUnit(tradingPlan.getAggregateInterval()));
            while (tradingPlan.getAggregateInterval().equals(AggregateInterval.DAILY) && (compare.getDayOfWeek().equals(DayOfWeek.SATURDAY) || compare.getDayOfWeek().equals(DayOfWeek.SUNDAY))) {
                compare = compare.plus(1, computeUnit(tradingPlan.getAggregateInterval()));
            }

            index += 1;
        }


        return aggregate(entries, (begin.isBefore(tradingPlan.getStartDate()) ? tradingPlan.getStartDate() : begin),  (limit.isAfter(tradingPlan.getEndDate()) ? tradingPlan.getEndDate() : limit), interval);
    }

    /**
     * Creates a new {@link TradingPlan} from the given {@link Map} of data
     *
     * @param data {@link Map}
     * @return newly created {@link TradingPlan}
     */
    public TradingPlan createTradingPlan(final Map<String, Object> data) {

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for creating a TradingPlan was null or empty");
        }

        try {
            TradingPlan tradingPlan = this.tradingPlanRepository.save(applyChanges(new TradingPlan(), data));

            //  set all other tradingPlans as inactive
            if (tradingPlan.isActive()) {
                this.tradingPlanRepository.resetTradingPlans(this.traderBuddyUserDetailsService.getCurrentUser().getAccount());
            }

            return tradingPlan;
        } catch (Exception e) {
            throw new EntityCreationException(String.format("A TradingPlan entity could not be created : %s" , e.getMessage()), e);
        }
    }

    /**
     * Updates an existing {@link TradingPlan} with the given {@link Map} of data. Update methods are designed to be idempotent.
     *
     * @param name name
     * @param start {@link LocalDate}
     * @param end {@link LocalDate}
     * @param data {@link Map}
     * @return modified {@link TradingPlan}
     */
    public TradingPlan updateTradingPlan(final String name, final LocalDate start, final LocalDate end, final Map<String, Object> data) {

        validateParameterIsNotNull(name, "name cannot be null");
        validateParameterIsNotNull(start, CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start.atStartOfDay(), end.atStartOfDay(), CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for updating the TradingPlan was null or empty");
        }

        try {
            TradingPlan tradingPlan =
                    findTradingPlanForNameAndStartDateAndEndDate(name, start, end)
                            .orElseThrow(() -> new NoResultFoundException(String.format("No TradingPlan found for name %s , start date %s and end date %s", name, start.format(DateTimeFormatter.ISO_DATE), end.format(DateTimeFormatter.ISO_DATE))));

            return applyChanges(tradingPlan, data);
        } catch (Exception e) {
            throw new EntityModificationException(String.format("An error occurred while modifying the TradingPlan : %s" , e.getMessage()), e);
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
    private TradingPlan applyChanges(final TradingPlan tradingPlan, final Map<String, Object> data) {

        Map<String, Object> plan = (Map<String, Object>) data.get("plan");

        tradingPlan.setStatus(TradingPlanStatus.valueOf(plan.get("status").toString().toUpperCase()));
        tradingPlan.setActive(Boolean.parseBoolean(plan.get("active").toString()));
        tradingPlan.setAbsolute(Boolean.parseBoolean(plan.get("absolute").toString()));
        tradingPlan.setName(plan.get("name").toString());
        tradingPlan.setStartDate(LocalDate.parse(plan.get("startDate").toString(), DateTimeFormatter.ISO_DATE));
        tradingPlan.setEndDate(LocalDate.parse(plan.get("endDate").toString(), DateTimeFormatter.ISO_DATE));
        tradingPlan.setProfitTarget(BigDecimal.valueOf(Double.parseDouble(plan.get("profitTarget").toString())).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
        tradingPlan.setAggregateInterval(AggregateInterval.valueOf(plan.get("aggregateInterval").toString()));
        tradingPlan.setStartingBalance(BigDecimal.valueOf(Double.parseDouble(plan.get("startingBalance").toString())).setScale(2, RoundingMode.HALF_EVEN).doubleValue());

        if (data.containsKey("depositPlan")) {
            DepositPlan depositPlan = new DepositPlan();
            Map<String, Object> depPlan = (Map<String, Object>) data.get("depositPlan");

            depositPlan.setAmount(BigDecimal.valueOf(Double.parseDouble(depPlan.get("amount").toString())).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
            depositPlan.setAggregateInterval(AggregateInterval.valueOf(depPlan.get("frequency").toString().toUpperCase()));
            depositPlan.setAbsolute(Boolean.parseBoolean(depPlan.get("absolute").toString()));

            depositPlan = this.depositPlanRepository.save(depositPlan);
            tradingPlan.setDepositPlan(depositPlan);
        }

        if (data.containsKey("withdrawalPlan")) {
            WithdrawalPlan withdrawalPlan = new WithdrawalPlan();
            Map<String, Object> witPlan = (Map<String, Object>) data.get("withdrawalPlan");

            withdrawalPlan.setAmount(BigDecimal.valueOf(Double.parseDouble(witPlan.get("amount").toString())).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
            withdrawalPlan.setAbsolute(Boolean.parseBoolean(witPlan.get("absolute").toString()));
            withdrawalPlan.setAggregateInterval(AggregateInterval.valueOf(witPlan.get("frequency").toString().toUpperCase()));

            withdrawalPlan = this.withdrawalPlanRepository.save(withdrawalPlan);
            tradingPlan.setWithdrawalPlan(withdrawalPlan);
        }

        return this.tradingPlanRepository.save(tradingPlan);
    }

    /**
     * Returns the {@link TemporalUnit} for the given {@link CompoundFrequency}
     *
     * @param interval {@link AggregateInterval}
     * @return {@link TemporalUnit}
     */
    private TemporalUnit computeUnit(AggregateInterval interval) {
        return
                switch (interval) {
                    case DAILY -> ChronoUnit.DAYS;
                    case WEEKLY -> ChronoUnit.WEEKS;
                    case MONTHLY -> ChronoUnit.MONTHS;
                    default -> ChronoUnit.YEARS;
                };
    }

    /**
     * Computes an appropriate start date base on the given date and {@link CompoundFrequency}
     *
     * @param date      {@link LocalDate}
     * @param frequency {@link CompoundFrequency}
     * @return {@link LocalDate}
     */
    private LocalDate computeStart(LocalDate date, AggregateInterval frequency) {
        return
                switch (frequency) {
                    case DAILY -> {
                        while (date.getDayOfWeek().equals(DayOfWeek.SATURDAY) || date.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                            date = date.plusDays(1);
                        }

                        yield date;
                    }
                    case WEEKLY -> {
                        while (!date.getDayOfWeek().equals(DayOfWeek.MONDAY)) {
                            date = date.plusDays(1);
                        }

                        yield date;
                    }
                    case MONTHLY -> date.with(TemporalAdjusters.firstDayOfMonth());
                    default -> date;
                };
    }

    /**
     * Computes the deposit balance
     *
     * @param tradingPlan {@link TradingPlan}
     * @param compare {@link LocalDate}
     * @param balance current balance
     * @param index index
     * @return new balance
     */
    private BigDecimal computeDepositBalance(final TradingPlan tradingPlan, final LocalDate compare, final BigDecimal balance, final Integer index) {
        if (tradingPlan.getDepositPlan() != null && isFirstBusinessDayOfMonth(compare) && index != 0) {
            return balance.add(BigDecimal.valueOf(tradingPlan.getDepositPlan().getAmount()));
        }

        return balance;
    }

    /**
     * Computes the withdrawal balance
     *
     * @param tradingPlan {@link TradingPlan}
     * @param compare {@link LocalDate}
     * @param balance current balance
     * @param index index
     * @return new balance
     */
    private BigDecimal computeWithdrawalBalance(final TradingPlan tradingPlan, final LocalDate compare, final BigDecimal balance, final Integer index) {
        if (tradingPlan.getWithdrawalPlan() != null && isFirstBusinessDayOfMonth(compare) && index != 0) {
            return balance.subtract(BigDecimal.valueOf(tradingPlan.getWithdrawalPlan().getAmount()));
        }

        return balance;
    }

    /**
     * Computes the first Monday of the month
     *
     * @param compare {@link LocalDate}
     * @return true if the given date is the first monday of the month
     */
    private boolean isFirstBusinessDayOfMonth(final LocalDate compare) {
        LocalDate firstBusinessDayOfMonth = compare.with(TemporalAdjusters.firstDayOfMonth());
        while (firstBusinessDayOfMonth.getDayOfWeek().equals(DayOfWeek.SATURDAY) || firstBusinessDayOfMonth.getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
            firstBusinessDayOfMonth = firstBusinessDayOfMonth.plusDays(1);
        }

        return firstBusinessDayOfMonth.isEqual(compare);
    }

    /**
     * Aggregates a {@link List} of {@link ForecastEntry}s for an {@link AggregateInterval}. The idea here is to generate a forecast for daily compounding (for example) but then display it on a per month basis
     *
     * @param entries {@link List} of {@link ForecastEntry}s
     * @param start start of interval
     * @param end end of interval
     * @param interval {@link AggregateInterval}
     * @return {@link List} of {@link ForecastEntry}s
     */
    private List<ForecastEntry> aggregate(final List<ForecastEntry> entries, final LocalDate start, final LocalDate end, final AggregateInterval interval) {

        if (CollectionUtils.isEmpty(entries)) {
            return Collections.emptyList();
        }

        List<ForecastEntry> result = new ArrayList<>();
        LocalDate compare = computeAddition(start, interval);

        result.add(new ForecastEntry(start, compare, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
        while (compare.isBefore(end)) {
            result.add(new ForecastEntry(compare, compare.plus(1L, computeUnit(interval)), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0));
            compare = computeAddition(compare, interval);
        }

        BigDecimal e;
        BigDecimal b = BigDecimal.valueOf(entries.get(0).balance()).subtract(BigDecimal.valueOf(entries.get(0).netEarnings()));
        BigDecimal d;
        BigDecimal w;

        for (int i = 0; i < result.size(); i++) {

            e = BigDecimal.ZERO;
            d = BigDecimal.ZERO;
            w = BigDecimal.ZERO;

            for (ForecastEntry child : entries) {
                if (
                        (child.startDate().isEqual(result.get(i).startDate()) || child.startDate().isAfter(result.get(i).startDate())) &&
                                (child.endDate().isBefore(result.get(i).endDate()) || child.endDate().isEqual(result.get(i).endDate()))
                ) {
                    e = e.add(BigDecimal.valueOf(child.earnings()));
                    d = d.add(BigDecimal.valueOf(child.deposits()));
                    w = w.add(BigDecimal.valueOf(child.withdrawals()));
                }
            }

            b = b.add(e).add(d).subtract(w);

            result.set(
                    i,
                    new ForecastEntry(
                            result.get(i).startDate(),
                            result.get(i).endDate(),
                            e.setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                            e.add(d).setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                            b.setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                            d.setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                            w.setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                            result.get(i).goal()
                    )
            );
        }

        return result;
    }

    /**
     * Computes how to manipulate the given {@link LocalDate} based on the given {@link AggregateInterval}
     *
     * @param compare {@link LocalDate}
     * @param interval {@link AggregateInterval}
     * @return adjusted {@link LocalDate}
     */
    private LocalDate computeAddition(final LocalDate compare, final AggregateInterval interval) {

        if (interval.equals(AggregateInterval.YEARLY)) {
            return compare.plusMonths(ChronoUnit.MONTHS.between(compare, compare.plusYears(1L).with(TemporalAdjusters.firstDayOfYear())));
        }

        return compare.plus(1L, computeUnit(interval));
    }
}
