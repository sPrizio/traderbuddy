package com.stephenprizio.traderbuddy.services.calculator;

import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.exceptions.calculator.UnexpectedNegativeValueException;
import com.stephenprizio.traderbuddy.exceptions.calculator.UnexpectedZeroValueException;
import com.stephenprizio.traderbuddy.exceptions.validation.IllegalParameterException;
import com.stephenprizio.traderbuddy.models.records.calculator.FinancingInfoRecord;
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

        FinancingInfoRecord record1 = new FinancingInfoRecord(1.0, 1.0, CompoundFrequency.WEEKLY, 1);
        FinancingInfoRecord record2 = new FinancingInfoRecord(null, 1.0, CompoundFrequency.WEEKLY, 1);
        FinancingInfoRecord record3 = new FinancingInfoRecord(1.0, null, CompoundFrequency.WEEKLY, 1);
        FinancingInfoRecord record4 = new FinancingInfoRecord(1.0, 1.0, null, 1);
        FinancingInfoRecord record5 = new FinancingInfoRecord(1.0, 1.0, CompoundFrequency.WEEKLY, null);

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
                .withMessage("compound frequency cannot be null");
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.compoundInterestCalculator.computeSchedule(TEST_DATE, record5))
                .withMessage("period cannot be null");
    }

    @Test
    public void test_computeSchedule_negativeParams() {

        FinancingInfoRecord record1 = new FinancingInfoRecord(-1.0, 1.0, CompoundFrequency.WEEKLY, 1);
        FinancingInfoRecord record2 = new FinancingInfoRecord(1.0, -1.0, CompoundFrequency.WEEKLY, 1);
        FinancingInfoRecord record3 = new FinancingInfoRecord(1.0, 1.0, CompoundFrequency.WEEKLY, -1);

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

        FinancingInfoRecord record1 = new FinancingInfoRecord(0.0, 1.0, CompoundFrequency.WEEKLY, 1);
        FinancingInfoRecord record2 = new FinancingInfoRecord(1.0, 0.0, CompoundFrequency.WEEKLY, 1);
        FinancingInfoRecord record3 = new FinancingInfoRecord(1.0, 1.0, CompoundFrequency.WEEKLY, 0);

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

        FinancingInfoRecord record = new FinancingInfoRecord(10000.0, 5.0, CompoundFrequency.MONTHLY, 15);
        assertThat(this.compoundInterestCalculator.computeSchedule(LocalDate.of(2022, 9, 6), record))
                .isNotEmpty()
                .element(6)
                .extracting("periodIndex", "interest", "accruedInterest", "balance")
                .containsExactly(6, 670.05, 4071.0, 14071.0);
    }


    //  ----------------- computeTotal -----------------

    @Test
    public void test_computeTotal_success() {

        FinancingInfoRecord record1 = new FinancingInfoRecord(10000.0, 12.0, CompoundFrequency.MONTHLY, 12);
        FinancingInfoRecord record2 = new FinancingInfoRecord(5000.0, 27.0, CompoundFrequency.BI_WEEKLY, 12);
        FinancingInfoRecord record3 = new FinancingInfoRecord(25000.0, 8.0, CompoundFrequency.QUARTERLY, 12);

        assertThat(this.compoundInterestCalculator.computeTotal(record1).setScale(2, RoundingMode.HALF_EVEN).doubleValue()).isEqualTo(38959.76);
        assertThat(this.compoundInterestCalculator.computeTotal(record2).setScale(2, RoundingMode.HALF_EVEN).doubleValue()).isEqualTo(88026.75);
        assertThat(this.compoundInterestCalculator.computeTotal(record3).setScale(2, RoundingMode.HALF_EVEN).doubleValue()).isEqualTo(62954.25);
    }


    //  ----------------- computeInterest -----------------

    @Test
    public void test_computeInterest_success() {

        FinancingInfoRecord record1 = new FinancingInfoRecord(10000.0, 12.0, CompoundFrequency.MONTHLY, 12);
        FinancingInfoRecord record2 = new FinancingInfoRecord(5000.0, 27.0, CompoundFrequency.BI_WEEKLY, 12);
        FinancingInfoRecord record3 = new FinancingInfoRecord(25000.0, 8.0, CompoundFrequency.QUARTERLY, 12);

        assertThat(this.compoundInterestCalculator.computeInterest(record1).setScale(2, RoundingMode.HALF_EVEN).doubleValue()).isEqualTo(28959.76);
        assertThat(this.compoundInterestCalculator.computeInterest(record2).setScale(2, RoundingMode.HALF_EVEN).doubleValue()).isEqualTo(83026.75);
        assertThat(this.compoundInterestCalculator.computeInterest(record3).setScale(2, RoundingMode.HALF_EVEN).doubleValue()).isEqualTo(37954.25);
    }


    //  ----------------- daysInYearExcludeWeekends -----------------

    @Test
    public void test_daysInYearExcludeWeekends_success() {
        assertThat(CompoundInterestCalculator.daysInYearExcludeWeekends()).isEqualTo(260);
    }
}
