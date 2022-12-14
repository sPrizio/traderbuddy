package com.traderbuddyv2.core.services.calculator;

import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.exceptions.calculator.UnexpectedNegativeValueException;
import com.traderbuddyv2.core.exceptions.calculator.UnexpectedZeroValueException;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import com.traderbuddyv2.core.models.records.calculator.FinancingInfo;
import org.junit.Test;

import java.math.RoundingMode;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link CompoundInterestCalculator}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class CompoundInterestCalculatorTest {

    private final LocalDate TEST_DATE = LocalDate.of(2022, 9, 6);

    private final CompoundInterestCalculator compoundInterestCalculator = new CompoundInterestCalculator();


    //  ----------------- computeSchedule -----------------

    @Test
    public void test_computeSchedule_missingParams() {

        FinancingInfo record1 = new FinancingInfo(1.0, 1.0, AggregateInterval.WEEKLY, 1);
        FinancingInfo record2 = new FinancingInfo(null, 1.0, AggregateInterval.WEEKLY, 1);
        FinancingInfo record3 = new FinancingInfo(1.0, null, AggregateInterval.WEEKLY, 1);
        FinancingInfo record4 = new FinancingInfo(1.0, 1.0, null, 1);
        FinancingInfo record5 = new FinancingInfo(1.0, 1.0, AggregateInterval.WEEKLY, null);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.compoundInterestCalculator.computeSchedule(null, record1))
                .withMessage("start date cannot be null");
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.compoundInterestCalculator.computeSchedule(TEST_DATE, record2))
                .withMessage("principal cannot be null");
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.compoundInterestCalculator.computeSchedule(TEST_DATE, record3))
                .withMessage("interest rate cannot be null");
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.compoundInterestCalculator.computeSchedule(TEST_DATE, record4))
                .withMessage("aggregate interval cannot be null");
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.compoundInterestCalculator.computeSchedule(TEST_DATE, record5))
                .withMessage("period cannot be null");
    }

    @Test
    public void test_computeSchedule_negativeParams() {

        FinancingInfo record1 = new FinancingInfo(-1.0, 1.0, AggregateInterval.WEEKLY, 1);
        FinancingInfo record2 = new FinancingInfo(1.0, -1.0, AggregateInterval.WEEKLY, 1);
        FinancingInfo record3 = new FinancingInfo(1.0, 1.0, AggregateInterval.WEEKLY, -1);

        assertThatExceptionOfType(UnexpectedNegativeValueException.class)
                .isThrownBy(() -> this.compoundInterestCalculator.computeSchedule(TEST_DATE, record1))
                .withMessage("principal cannot be negative");
        assertThatExceptionOfType(UnexpectedNegativeValueException.class)
                .isThrownBy(() -> this.compoundInterestCalculator.computeSchedule(TEST_DATE, record2))
                .withMessage("interest rate cannot be negative");
        assertThatExceptionOfType(UnexpectedNegativeValueException.class)
                .isThrownBy(() -> this.compoundInterestCalculator.computeSchedule(TEST_DATE, record3))
                .withMessage("period cannot be negative");
    }

    @Test
    public void test_computeSchedule_zeroValueParams() {

        FinancingInfo record1 = new FinancingInfo(0.0, 1.0, AggregateInterval.WEEKLY, 1);
        FinancingInfo record2 = new FinancingInfo(1.0, 0.0, AggregateInterval.WEEKLY, 1);
        FinancingInfo record3 = new FinancingInfo(1.0, 1.0, AggregateInterval.WEEKLY, 0);

        assertThatExceptionOfType(UnexpectedZeroValueException.class)
                .isThrownBy(() -> this.compoundInterestCalculator.computeSchedule(TEST_DATE, record1))
                .withMessage("principal cannot be zero");
        assertThatExceptionOfType(UnexpectedZeroValueException.class)
                .isThrownBy(() -> this.compoundInterestCalculator.computeSchedule(TEST_DATE, record2))
                .withMessage("interest rate cannot be zero");
        assertThatExceptionOfType(UnexpectedZeroValueException.class)
                .isThrownBy(() -> this.compoundInterestCalculator.computeSchedule(TEST_DATE, record3))
                .withMessage("period cannot be zero");
    }

    @Test
    public void test_computeSchedule_success() {

        FinancingInfo record = new FinancingInfo(10000.0, 5.0, AggregateInterval.MONTHLY, 15);
        assertThat(this.compoundInterestCalculator.computeSchedule(LocalDate.of(2022, 9, 6), record))
                .isNotEmpty()
                .element(6)
                .extracting("periodIndex", "interest", "accruedInterest", "balance")
                .containsExactly(6, 670.05, 4071.0, 14071.0);
    }


    //  ----------------- computeTotal -----------------

    @Test
    public void test_computeTotal_success() {

        FinancingInfo record1 = new FinancingInfo(10000.0, 12.0, AggregateInterval.MONTHLY, 12);

        assertThat(this.compoundInterestCalculator.computeTotal(record1).setScale(2, RoundingMode.HALF_EVEN).doubleValue()).isEqualTo(38959.76);
    }


    //  ----------------- computeInterest -----------------

    @Test
    public void test_computeInterest_success() {

        FinancingInfo record1 = new FinancingInfo(10000.0, 12.0, AggregateInterval.MONTHLY, 12);

        assertThat(this.compoundInterestCalculator.computeInterest(record1).setScale(2, RoundingMode.HALF_EVEN).doubleValue()).isEqualTo(28959.76);
    }


    //  ----------------- daysInYearExcludeWeekends -----------------

    @Test
    public void test_daysInYearExcludeWeekends_success() {
        assertThat(CompoundInterestCalculator.daysInYearExcludeWeekends()).isEqualTo(260);
    }
}
