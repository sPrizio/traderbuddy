package com.stephenprizio.traderbuddy.services.investing;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.enums.plans.TradingPlanStatus;
import com.stephenprizio.traderbuddy.exceptions.validation.IllegalParameterException;
import com.stephenprizio.traderbuddy.models.entities.plans.TradingPlan;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link InvestingService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class InvestingServiceTest extends AbstractGenericTest {

    private final TradingPlan TEST_PLAN = new TradingPlan();

    @Resource(name = "investingService")
    private InvestingService investingService;

    @Before
    public void setUp() {
        TEST_PLAN.setActive(true);
        TEST_PLAN.setName("Test Trading Plan Active");
        TEST_PLAN.setStatus(TradingPlanStatus.IN_PROGRESS);
        TEST_PLAN.setStartingBalance(1000.0);
        TEST_PLAN.setDepositPlan(null);
        TEST_PLAN.setWithdrawalPlan(null);
    }


    //  ----------------- forecast -----------------

    @Test
    public void test_forecast_inactive() {
        TradingPlan tradingPlan = new TradingPlan();
        tradingPlan.setActive(false);
        assertThat(this.investingService.forecast(tradingPlan))
                .isEmpty();
    }

    @Test
    public void test_forecast_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.investingService.forecast(null))
                .withMessage("tradingPlan cannot be null");

        TradingPlan tradingPlan = new TradingPlan();
        tradingPlan.setActive(true);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.investingService.forecast(tradingPlan))
                .withMessage("compound frequency cannot be null");
        tradingPlan.setCompoundFrequency(CompoundFrequency.DAILY);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.investingService.forecast(tradingPlan))
                .withMessage("profit target cannot be null");
        tradingPlan.setProfitTarget(1.0);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.investingService.forecast(tradingPlan))
                .withMessage("start date cannot be null");
        tradingPlan.setStartDate(LocalDate.MAX);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.investingService.forecast(tradingPlan))
                .withMessage("end date cannot be null");
        tradingPlan.setEndDate(LocalDate.MIN);

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.investingService.forecast(tradingPlan))
                .withMessage("start date cannot be after end date or vice versa");
    }

    @Test
    public void test_forecast_daily_success() {
        TEST_PLAN.setStartDate(LocalDate.of(2022, 1, 1));
        TEST_PLAN.setEndDate(LocalDate.of(2022, 7, 1));
        TEST_PLAN.setProfitTarget(1.25);
        TEST_PLAN.setCompoundFrequency(CompoundFrequency.DAILY_NO_WEEKENDS);

        assertThat(this.investingService.forecast(TEST_PLAN))
                .element(12)
                .extracting("earnings", "balance")
                .containsExactly(14.51, 1175.26);
    }

    @Test
    public void test_forecast_daily_with_deposit_and_withdrawal_success() {
        TEST_PLAN.setStartDate(LocalDate.of(2022, 1, 1));
        TEST_PLAN.setEndDate(LocalDate.of(2022, 7, 1));
        TEST_PLAN.setProfitTarget(1.25);
        TEST_PLAN.setCompoundFrequency(CompoundFrequency.DAILY_NO_WEEKENDS);
        TEST_PLAN.setDepositPlan(generateDepositPlan());
        TEST_PLAN.setWithdrawalPlan(generateWithdrawalPlan());

        assertThat(this.investingService.forecast(TEST_PLAN))
                .element(43)
                .extracting("earnings", "balance")
                .containsExactly(28.05, 2272.15);
    }

    @Test
    public void test_forecast_weekly_success() {
        TEST_PLAN.setStartDate(LocalDate.of(2022, 1, 1));
        TEST_PLAN.setEndDate(LocalDate.of(2022, 7, 1));
        TEST_PLAN.setProfitTarget(9.1);
        TEST_PLAN.setCompoundFrequency(CompoundFrequency.WEEKLY);

        assertThat(this.investingService.forecast(TEST_PLAN))
                .element(2)
                .extracting("earnings", "balance")
                .containsExactly(108.32, 1298.6);
    }

    @Test
    public void test_forecast_monthly_success() {
        TEST_PLAN.setStartDate(LocalDate.of(2022, 1, 1));
        TEST_PLAN.setEndDate(LocalDate.of(2022, 7, 1));
        TEST_PLAN.setProfitTarget(11.38);
        TEST_PLAN.setCompoundFrequency(CompoundFrequency.MONTHLY);

        assertThat(this.investingService.forecast(TEST_PLAN))
                .element(2)
                .extracting("earnings", "balance")
                .containsExactly(141.17, 1381.73);
    }
}
