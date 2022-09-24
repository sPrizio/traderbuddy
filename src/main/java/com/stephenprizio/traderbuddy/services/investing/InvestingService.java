package com.stephenprizio.traderbuddy.services.investing;

import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.models.entities.plans.TradingPlan;
import com.stephenprizio.traderbuddy.models.records.calculator.FinancingInfo;
import com.stephenprizio.traderbuddy.models.records.investing.ForecastEntry;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingRecord;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingRecordStatistics;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingSummary;
import com.stephenprizio.traderbuddy.services.calculator.CompoundInterestCalculator;
import com.stephenprizio.traderbuddy.services.summary.TradingSummaryService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.*;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer related to investing
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("investingService")
public class InvestingService {

    @Resource(name = "compoundInterestCalculator")
    private CompoundInterestCalculator compoundInterestCalculator;

    @Resource(name = "tradingSummaryService")
    public TradingSummaryService tradingSummaryService;


    //  METHODS

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

        validateParameterIsNotNull(tradingPlan, "tradingPlan cannot be null");

        if (Boolean.FALSE.equals(tradingPlan.getActive())) {
            return Collections.emptyList();
        }

        validateParameterIsNotNull(tradingPlan.getCompoundFrequency(), "compound frequency cannot be null");
        validateParameterIsNotNull(tradingPlan.getProfitTarget(), "trading plan profit target cannot be null");
        validateParameterIsNotNull(tradingPlan.getStartDate(), "trading plan start date cannot be null");
        validateParameterIsNotNull(tradingPlan.getEndDate(), "trading plan end date cannot be null");
        validateParameterIsNotNull(interval, "interval cannot be null");
        validateParameterIsNotNull(begin, "begin cannot be null");
        validateParameterIsNotNull(limit, "limit cannot be null");
        validateDatesAreNotMutuallyExclusive(begin.atStartOfDay(), limit.atStartOfDay(), "The start date was after the end date or vice versa");

        LocalDate startDate = computeStart(tradingPlan.getStartDate(), tradingPlan.getCompoundFrequency());
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

            BigDecimal earnings = this.compoundInterestCalculator.computeInterest(new FinancingInfo(balance.setScale(2, RoundingMode.HALF_EVEN).doubleValue(), tradingPlan.getProfitTarget(), tradingPlan.getCompoundFrequency(), 1));
            accruedEarnings = accruedEarnings.add(earnings);
            balance = balance.add(earnings);

            entries.add(
                    new ForecastEntry(
                            compare,
                            computeUnit(tradingPlan.getCompoundFrequency()).equals(ChronoUnit.WEEKS) ? compare.plus(6L, ChronoUnit.DAYS) : compare.plus(1L, computeUnit(tradingPlan.getCompoundFrequency())),
                            earnings.setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                            accruedEarnings.setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                            balance.setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                            computeDepositBalance(tradingPlan, compare, balance, index).equals(balance) ? 0.0 : tradingPlan.getDepositPlan().getAmount(),
                            computeWithdrawalBalance(tradingPlan, compare, balance, index).equals(balance) ? 0.0 : tradingPlan.getWithdrawalPlan().getAmount(),
                            tradingPlan.getProfitTarget()
                    )
            );

            compare = compare.plus(1, computeUnit(tradingPlan.getCompoundFrequency()));
            while (tradingPlan.getCompoundFrequency().equals(CompoundFrequency.DAILY_NO_WEEKENDS) && (compare.getDayOfWeek().equals(DayOfWeek.SATURDAY) || compare.getDayOfWeek().equals(DayOfWeek.SUNDAY))) {
                compare = compare.plus(1, computeUnit(tradingPlan.getCompoundFrequency()));
            }

            index += 1;
        }


        return aggregate(entries, (begin.isBefore(tradingPlan.getStartDate()) ? tradingPlan.getStartDate() : begin),  (limit.isAfter(tradingPlan.getEndDate()) ? tradingPlan.getEndDate() : limit), tradingPlan.getCompoundFrequency(), interval);
    }

    /**
     * Generates a {@link TradingSummary} for the given {@link TradingPlan} and time span
     * @param tradingPlan {@link TradingPlan}
     * @param interval {@link AggregateInterval}
     * @param begin start date for aggregation
     * @param limit end date for aggregation
     * @return {@link TradingSummary}
     */
    public TradingSummary obtainTradingPerformanceForForecast(final TradingPlan tradingPlan, final AggregateInterval interval, final LocalDate begin, final LocalDate limit) {

        List<ForecastEntry> entries = forecast(tradingPlan, interval, begin, limit);
        Map<LocalDate, ForecastEntry> entryMap = new HashMap<>();
        entries.forEach(e -> entryMap.put(e.startDate(), e));

        List<TradingRecord> records = this.tradingSummaryService.getReportOfSummariesForTimeSpan(begin.atStartOfDay(), limit.atStartOfDay(), interval).records();
        List<TradingRecord> result = new ArrayList<>();

        for (TradingRecord tradingRecord : records) {
            ForecastEntry tempEntry = entryMap.get(tradingRecord.start().toLocalDate());

            if (tempEntry != null) {
                BigDecimal percentageProfit = computePercentageProfit(tempEntry.earnings(), tradingRecord.netProfit(), tradingPlan.getProfitTarget());
                BigDecimal surplus = BigDecimal.valueOf(tradingRecord.netProfit()).subtract(BigDecimal.valueOf(tempEntry.earnings())).setScale(2, RoundingMode.HALF_EVEN);

                result.add(
                        new TradingRecord(
                                tradingRecord.start(),
                                tradingRecord.end(),
                                tempEntry.earnings(),
                                tradingRecord.numberOfTrades(),
                                tradingRecord.winPercentage(),
                                tradingRecord.netProfit(),
                                Boolean.TRUE.equals(isCompletedSession(tradingRecord)) ? percentageProfit.doubleValue() : 0.0,
                                Boolean.TRUE.equals(isCompletedSession(tradingRecord)) ? surplus.doubleValue() : 0.0,
                                tradingRecord.show(),
                                Boolean.TRUE.equals(isCompletedSession(tradingRecord)) ? percentageProfit.doubleValue() >= tradingPlan.getProfitTarget() : null
                        )
                );
            }
        }

        return new TradingSummary(result, new TradingRecordStatistics(result));
    }


    //  HELPERS

    /**
     * Returns the {@link TemporalUnit} for the given {@link CompoundFrequency}
     *
     * @param frequency {@link CompoundFrequency}
     * @return {@link TemporalUnit}
     */
    private TemporalUnit computeUnit(CompoundFrequency frequency) {
        return
                switch (frequency) {
                    case DAILY, DAILY_NO_WEEKENDS -> ChronoUnit.DAYS;
                    case WEEKLY -> ChronoUnit.WEEKS;
                    case MONTHLY -> ChronoUnit.MONTHS;
                    default -> ChronoUnit.YEARS;
                };
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
    private LocalDate computeStart(LocalDate date, CompoundFrequency frequency) {
        return
                switch (frequency) {
                    case DAILY, DAILY_NO_WEEKENDS -> {
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
    private Boolean isFirstBusinessDayOfMonth(final LocalDate compare) {
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
    private List<ForecastEntry> aggregate(final List<ForecastEntry> entries, final LocalDate start, final LocalDate end, final CompoundFrequency frequency, final AggregateInterval interval) {

        if (CollectionUtils.isEmpty(entries)) {
            return Collections.emptyList();
        }

        if (areIntervalsEqual(frequency, interval)) {
            return
                    recomputeEarnings(
                            entries
                                    .stream()
                                    .filter(e -> e.startDate().isEqual(start) || e.startDate().isAfter(start))
                                    .filter(e -> e.endDate().isBefore(end) || e.endDate().isEqual(end))
                                    .toList()
                    );
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
     * Looks for equivalence between the given {@link CompoundFrequency} & {@link AggregateInterval}
     *
     * @param frequency {@link CompoundFrequency}
     * @param interval {@link AggregateInterval}
     * @return true if they're equivalent
     */
    private Boolean areIntervalsEqual(final CompoundFrequency frequency, final AggregateInterval interval) {
        return switch (frequency) {
            case DAILY, DAILY_NO_WEEKENDS -> interval.equals(AggregateInterval.DAILY);
            case WEEKLY -> interval.equals(AggregateInterval.WEEKLY);
            case MONTHLY -> interval.equals(AggregateInterval.MONTHLY);
            case YEARLY -> interval.equals(AggregateInterval.YEARLY);
            default -> true;
        };
    }

    /**
     * Recomputes the earnings where necessary
     *
     * @param entries {@link List} of {@link ForecastEntry}
     * @return {@link List} of {@link ForecastEntry}
     */
    private List<ForecastEntry> recomputeEarnings(final List<ForecastEntry> entries) {

        if (CollectionUtils.isEmpty(entries)) {
            return Collections.emptyList();
        }

        BigDecimal decimal = BigDecimal.ZERO;
        List<ForecastEntry> result = new ArrayList<>(entries);

        for (int i = 0; i < result.size(); i++) {
            ForecastEntry temp = result.get(i);
            decimal = decimal.add(BigDecimal.valueOf(temp.earnings()));
            result.set(i, new ForecastEntry(temp.startDate(), temp.endDate(), temp.earnings(), decimal.setScale(2, RoundingMode.HALF_EVEN).doubleValue(), temp.balance(), temp.deposits(), temp.withdrawals(), temp.goal()));
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

    /**
     * Computes the percentage profit
     *
     * @param target {@link Double} target
     * @param result {@link Double} result
     * @param targetPercentage {@link Double} percentage
     * @return {@link BigDecimal}
     */
    private BigDecimal computePercentageProfit(final Double target, final Double result, final Double targetPercentage) {

        if (result == 0.0) {
            return BigDecimal.ZERO;
        }

        return BigDecimal.valueOf(1).subtract(BigDecimal.valueOf(target).divide(BigDecimal.valueOf(result), RoundingMode.HALF_EVEN)).add(BigDecimal.valueOf(targetPercentage));
    }

    /**
     * Returns true if the current day is not referenced in the {@link TradingRecord}
     *
     * @param tradingRecord {@link TradingRecord}
     * @return true if today != trading record period
     */
    public Boolean isCompletedSession(final TradingRecord tradingRecord) {
        LocalDate now = LocalDate.now();
        return tradingRecord.start().isBefore(now.atStartOfDay()) && (tradingRecord.end().isBefore(now.atStartOfDay()) || tradingRecord.end().isEqual(now.atStartOfDay()));
    }
}
