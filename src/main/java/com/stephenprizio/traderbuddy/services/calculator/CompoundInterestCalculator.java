package com.stephenprizio.traderbuddy.services.calculator;

import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.models.records.calculator.CompoundedInterestRecord;
import com.stephenprizio.traderbuddy.models.records.calculator.FinancingInfoRecord;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.*;

/**
 * Service for computing compound interest
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("compoundInterestCalculator")
public class CompoundInterestCalculator {


    //  METHODS

    /**
     * Computes a compounding interest schedule
     *
     * @param start       starting date {@link LocalDate}
     * @param financeInfo {@link FinancingInfoRecord}
     * @return {@link List} of {@link CompoundedInterestRecord}s
     */
    public List<CompoundedInterestRecord> computeSchedule(final LocalDate start, final FinancingInfoRecord financeInfo) {

        validateParameterIsNotNull(start, "start date cannot be null");
        validateFinancialInfo(financeInfo);

        LocalDate compare = start.with(TemporalAdjusters.firstDayOfMonth()).plusMonths(1);
        LocalDate end = start.plusMonths((financeInfo.period() + 1)).with(TemporalAdjusters.firstDayOfMonth()).minusDays(1);

        BigDecimal accruedInterest = BigDecimal.ZERO;
        BigDecimal balance = BigDecimal.valueOf(financeInfo.principal());

        List<CompoundedInterestRecord> records = new ArrayList<>();
        while (compare.isBefore(end) || compare.isEqual(end)) {

            BigDecimal interest = computeInterest(new FinancingInfoRecord(balance.setScale(2, RoundingMode.HALF_EVEN).doubleValue(), financeInfo.interestRate(), financeInfo.compoundFrequency(), 1));
            accruedInterest = accruedInterest.add(interest);
            balance = balance.add(interest);

            records.add(
                    new CompoundedInterestRecord(
                            (int) ChronoUnit.MONTHS.between(start, compare),
                            interest.setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                            accruedInterest.setScale(2, RoundingMode.HALF_EVEN).doubleValue(),
                            balance.setScale(2, RoundingMode.HALF_EVEN).doubleValue()
                    )
            );

            compare = compare.plusMonths(1);
        }

        return records;
    }

    /**
     * Computes the future value of the given principal by applying the compound interest formula
     *
     * @param financeInfo {@link FinancingInfoRecord}
     * @return future value of principal expressed as a {@link BigDecimal}
     */
    public BigDecimal computeTotal(final FinancingInfoRecord financeInfo) {

        final BigDecimal p = BigDecimal.valueOf(financeInfo.principal());
        final BigDecimal r = BigDecimal.valueOf(financeInfo.interestRate() > 1.0 ? BigDecimal.valueOf(financeInfo.interestRate()).divide(BigDecimal.valueOf(100.0), 10, RoundingMode.HALF_EVEN).doubleValue() : financeInfo.interestRate());
        final BigDecimal n = BigDecimal.valueOf(financeInfo.compoundFrequency().getFrequency());
        final BigDecimal t = BigDecimal.valueOf(financeInfo.period()).divide(BigDecimal.valueOf(12.0), 10, RoundingMode.HALF_EVEN);

        final double result = Math.pow(BigDecimal.ONE.add(r).setScale(10, RoundingMode.HALF_EVEN).doubleValue(), Double.valueOf(financeInfo.period()));
        return BigDecimal.valueOf(result).multiply(p);
    }

    /**
     * Computes the interest gain on principal by applying the compounding interest formula
     *
     * @param financeInfo {@link FinancingInfoRecord}
     * @return interest gain expressed as a {@link BigDecimal}
     */
    public BigDecimal computeInterest(final FinancingInfoRecord financeInfo) {
        return computeTotal(financeInfo).subtract(BigDecimal.valueOf(financeInfo.principal()));
    }

    /**
     * Computes the number of days in the year excluding Saturday and Sunday
     *
     * @return number of days in year Mon-Fri
     */
    public static int daysInYearExcludeWeekends() {
        LocalDate start = LocalDate.now().with(TemporalAdjusters.firstDayOfYear());
        return (int) start.datesUntil(start.plusYears(1)).filter(d -> (!d.getDayOfWeek().equals(DayOfWeek.SATURDAY) && !d.getDayOfWeek().equals(DayOfWeek.SUNDAY))).count();
    }


    //  HELPERS

    /**
     * Validates the given {@link FinancingInfoRecord}
     *
     * @param financingInfo {@link FinancingInfoRecord}
     */
    private void validateFinancialInfo(final FinancingInfoRecord financingInfo) {
        validateParameterIsNotNull(financingInfo.principal(), "principal cannot be null");
        validateParameterIsNotNull(financingInfo.interestRate(), "interest rate cannot be null");
        validateParameterIsNotNull(financingInfo.compoundFrequency(), "compound frequency cannot be null");
        validateParameterIsNotNull(financingInfo.period(), "period cannot be null");

        validateNonNegativeValue(financingInfo.principal(), "principal cannot be negative");
        validateNonNegativeValue(financingInfo.interestRate(), "interest rate cannot be negative");
        validateNonNegativeValue(financingInfo.period(), "period cannot be negative");

        validateNonZeroValue(financingInfo.principal(), "principal cannot be zero");
        validateNonZeroValue(financingInfo.interestRate(), "interest rate cannot be zero");
        validateNonZeroValue(financingInfo.period(), "period cannot be zero");
    }
}
