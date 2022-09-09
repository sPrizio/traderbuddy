package com.stephenprizio.traderbuddy.services.investing;

import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.models.entities.plans.TradingPlan;
import com.stephenprizio.traderbuddy.models.records.calculator.FinancingInfoRecord;
import com.stephenprizio.traderbuddy.models.records.investing.ForecastEntry;
import com.stephenprizio.traderbuddy.services.calculator.CompoundInterestCalculator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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


    //  METHODS

    public List<ForecastEntry> forecast(final TradingPlan tradingPlan) {

        validateParameterIsNotNull(tradingPlan, "tradingPlan cannot be null");

        if (Boolean.FALSE.equals(tradingPlan.getActive())) {
            return Collections.emptyList();
        }

        validateParameterIsNotNull(tradingPlan.getCompoundFrequency(), "compound frequency cannot be null");
        validateParameterIsNotNull(tradingPlan.getProfitTarget(), "profit target cannot be null");
        validateParameterIsNotNull(tradingPlan.getStartDate(), "start date cannot be null");
        validateParameterIsNotNull(tradingPlan.getEndDate(), "end date cannot be null");

        LocalDate startDate = computeStart(tradingPlan.getStartDate(), tradingPlan.getCompoundFrequency());
        LocalDate endDate = tradingPlan.getEndDate();
        LocalDate compare = startDate;

        validateDatesAreNotMutuallyExclusive(startDate.atStartOfDay(), endDate.atStartOfDay(), "start date cannot be after end date or vice versa");

        List<ForecastEntry> entries = new ArrayList<>();
        BigDecimal accruedEarnings = BigDecimal.ZERO;
        BigDecimal balance = BigDecimal.valueOf(tradingPlan.getStartingBalance());

        while (compare.isBefore(endDate)) {

            balance = computeDepositBalance(tradingPlan, compare, balance);
            balance = computeWithdrawalBalance(tradingPlan, compare, balance);

            BigDecimal earnings = this.compoundInterestCalculator.computeInterest(new FinancingInfoRecord(balance.setScale(2, RoundingMode.HALF_EVEN).doubleValue(), tradingPlan.getProfitTarget(), tradingPlan.getCompoundFrequency(), 1));
            accruedEarnings = accruedEarnings.add(earnings);
            balance = balance.add(earnings);

            entries.add(
                    new ForecastEntry(
                            compare,
                            computeUnit(tradingPlan.getCompoundFrequency()).equals(ChronoUnit.WEEKS) ? compare.plus(6L, ChronoUnit.DAYS) : compare.plus(1L, computeUnit(tradingPlan.getCompoundFrequency())),
                            earnings.setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                            accruedEarnings.setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                            balance.setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                            computeDepositBalance(tradingPlan, compare, balance).equals(balance) ? 0.0 : tradingPlan.getDepositPlan().getAmount(),
                            computeWithdrawalBalance(tradingPlan, compare, balance).equals(balance) ? 0.0 : tradingPlan.getWithdrawalPlan().getAmount()
                    )
            );

            compare = compare.plus(1, computeUnit(tradingPlan.getCompoundFrequency()));
            while (tradingPlan.getCompoundFrequency().equals(CompoundFrequency.DAILY_NO_WEEKENDS) && (compare.getDayOfWeek().equals(DayOfWeek.SATURDAY) || compare.getDayOfWeek().equals(DayOfWeek.SUNDAY))) {
                compare = compare.plus(1, computeUnit(tradingPlan.getCompoundFrequency()));
            }
        }

        return entries;
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

    private BigDecimal computeDepositBalance(final TradingPlan tradingPlan, final LocalDate compare, final BigDecimal balance) {
        if (tradingPlan.getDepositPlan() != null && compare.getDayOfMonth() == 1) {
            return balance.add(BigDecimal.valueOf(tradingPlan.getDepositPlan().getAmount()));
        }

        return balance;
    }

    private BigDecimal computeWithdrawalBalance(final TradingPlan tradingPlan, final LocalDate compare, final BigDecimal balance) {
        if (tradingPlan.getWithdrawalPlan() != null && compare.getDayOfMonth() == 1) {
            return balance.subtract(BigDecimal.valueOf(tradingPlan.getWithdrawalPlan().getAmount()));
        }

        return balance;
    }
}
