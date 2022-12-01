package com.traderbuddyv2.core.services.plan;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.enums.plans.TradingPlanStatus;
import com.traderbuddyv2.core.exceptions.system.EntityCreationException;
import com.traderbuddyv2.core.exceptions.system.EntityModificationException;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import com.traderbuddyv2.core.exceptions.validation.MissingRequiredDataException;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.plan.TradingPlan;
import com.traderbuddyv2.core.repositories.plan.TradingPlanRepository;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link TradingPlanService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TradingPlanServiceTest extends AbstractGenericTest {

    private static final String TEST_NAME = "Test Trading Plan Active";
    private static final LocalDate TEST_START = LocalDate.of(2022, 1, 1);
    private static final LocalDate TEST_END = LocalDate.of(2025, 1, 1);
    private static final Double TEST_PROFIT = 1.25;
    private static final AggregateInterval TEST_FREQUENCY = AggregateInterval.DAILY;

    private final TradingPlan TEST_TRADING_PLAN_ACTIVE = generateTestTradingPlan();

    private final Account TEST_ACCOUNT = generateTestAccount();

    private final TradingPlan TEST_PLAN = new TradingPlan();

    @MockBean
    private TradingPlanRepository tradingPlanRepository;

    @MockBean
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @Autowired
    private TradingPlanService tradingPlanService;

    @Before
    public void setUp() {
        TEST_TRADING_PLAN_ACTIVE.setAccount(TEST_ACCOUNT);
        Mockito.when(this.tradingPlanRepository.findTopTradingPlanByActiveIsTrueAndAccountOrderByStartDateDesc(any())).thenReturn(TEST_TRADING_PLAN_ACTIVE);
        Mockito.when(this.tradingPlanRepository.findAllByStatusAndAccount(any(), any())).thenReturn(List.of(TEST_TRADING_PLAN_ACTIVE));
        Mockito.when(this.tradingPlanRepository.findTradingPlanByNameAndStartDateAndEndDateAndAccount(any(), any(), any(), any())).thenReturn(TEST_TRADING_PLAN_ACTIVE);
        Mockito.when(this.tradingPlanRepository.save(any())).thenReturn(TEST_TRADING_PLAN_ACTIVE);
        Mockito.when(this.traderBuddyUserDetailsService.getCurrentUser()).thenReturn(generateTestUser());

        TEST_PLAN.setActive(true);
        TEST_PLAN.setName("Test Trading Plan Active");
        TEST_PLAN.setStatus(TradingPlanStatus.IN_PROGRESS);
        TEST_PLAN.setStartingBalance(1000.0);
        TEST_PLAN.setDepositPlan(null);
        TEST_PLAN.setWithdrawalPlan(null);
    }


    //  ----------------- findTradingPlanByActiveIsTrue -----------------

    @Test
    public void test_findTradingPlanByActiveIsTrue_success() {
        assertThat(this.tradingPlanService.findCurrentlyActiveTradingPlan())
                .isPresent()
                .map(TradingPlan::getProfitTarget)
                .get()
                .isEqualTo(TEST_PROFIT);
    }


    //  ----------------- findTradingPlansForStatus -----------------

    @Test
    public void test_findTradingPlansForStatus_missingParamStatus() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradingPlanService.findTradingPlansForStatus(null))
                .withMessage("tradingPlan status cannot be null");
    }

    @Test
    @WithMockUser(username = "test")
    public void test_findTradingPlansForStatus_success() {
        assertThat(this.tradingPlanService.findTradingPlansForStatus(TradingPlanStatus.IN_PROGRESS))
                .hasSize(1)
                .extracting("profitTarget", "name")
                .contains(Tuple.tuple(TEST_PROFIT, TEST_NAME));
    }


    //  ----------------- findTradingPlanForNameAndStartDateAndEndDate -----------------

    @Test
    public void test_findTradingPlanForNameAndStartDateAndEndDate_missingParamName() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradingPlanService.findTradingPlanForNameAndStartDateAndEndDate(null, LocalDate.MIN, LocalDate.MAX))
                .withMessage("name cannot be null");
    }

    @Test
    public void test_findTradingPlanForNameAndStartDateAndEndDate_missingParamStartDate() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradingPlanService.findTradingPlanForNameAndStartDateAndEndDate("name", null, LocalDate.MAX))
                .withMessage(CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
    }

    @Test
    public void test_findTradingPlanForNameAndStartDateAndEndDate_missingParamEndDate() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradingPlanService.findTradingPlanForNameAndStartDateAndEndDate("name", LocalDate.MIN, null))
                .withMessage(CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    public void test_findTradingPlanForNameAndStartDateAndEndDate_badDates() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradingPlanService.findTradingPlanForNameAndStartDateAndEndDate("name", LocalDate.MAX, LocalDate.MIN))
                .withMessage(CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    public void test_findTradingPlanForNameAndStartDateAndEndDate_success() {
        assertThat(this.tradingPlanService.findTradingPlanForNameAndStartDateAndEndDate(TEST_NAME, TEST_START, TEST_END))
                .isNotNull()
                .get()
                .extracting("profitTarget", "name")
                .containsExactly(TEST_PROFIT, TEST_NAME);
    }

    
    //  ----------------- forecast -----------------

    @Test
    public void test_forecast_inactive() {
        TradingPlan tradingPlan = new TradingPlan();
        tradingPlan.setActive(false);
        assertThat(this.tradingPlanService.forecast(tradingPlan, AggregateInterval.MONTHLY, LocalDate.MIN, LocalDate.MAX))
                .isEmpty();
    }

    @Test
    public void test_forecast_missingParams() {

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradingPlanService.forecast(null, AggregateInterval.MONTHLY, LocalDate.MIN, LocalDate.MAX))
                .withMessage("trading plan cannot be null");

        TradingPlan tradingPlan = new TradingPlan();
        tradingPlan.setActive(true);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradingPlanService.forecast(tradingPlan, AggregateInterval.MONTHLY, LocalDate.MIN, LocalDate.MAX))
                .withMessage("aggregate interval cannot be null");
        tradingPlan.setAggregateInterval(AggregateInterval.DAILY);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradingPlanService.forecast(tradingPlan, AggregateInterval.MONTHLY, LocalDate.MIN, LocalDate.MAX))
                .withMessage("trading plan start date cannot be null");
        tradingPlan.setStartDate(LocalDate.MAX);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradingPlanService.forecast(tradingPlan, AggregateInterval.MONTHLY, LocalDate.MIN, LocalDate.MAX))
                .withMessage("trading plan end date cannot be null");
        tradingPlan.setEndDate(LocalDate.MIN);

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradingPlanService.forecast(tradingPlan, AggregateInterval.MONTHLY, LocalDate.MIN, LocalDate.MAX))
                .withMessage("start date cannot be after end date or vice versa");

        tradingPlan.setStartDate(LocalDate.MIN);
        tradingPlan.setEndDate(LocalDate.MAX);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradingPlanService.forecast(tradingPlan, null, LocalDate.MIN, LocalDate.MAX))
                .withMessage("interval cannot be null");

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradingPlanService.forecast(tradingPlan, AggregateInterval.MONTHLY, null, LocalDate.MAX))
                .withMessage("begin cannot be null");

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradingPlanService.forecast(tradingPlan, AggregateInterval.MONTHLY, LocalDate.MIN, null))
                .withMessage("limit cannot be null");

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradingPlanService.forecast(tradingPlan, AggregateInterval.MONTHLY, LocalDate.MAX, LocalDate.MIN))
                .withMessage("The start date was after the end date or vice versa");
    }

    @Test
    public void test_forecast_daily_success() {
        TEST_PLAN.setStartDate(LocalDate.of(2022, 1, 1));
        TEST_PLAN.setEndDate(LocalDate.of(2022, 7, 1));
        TEST_PLAN.setProfitTarget(1.25);
        TEST_PLAN.setAggregateInterval(AggregateInterval.DAILY);

        assertThat(this.tradingPlanService.forecast(TEST_PLAN, AggregateInterval.DAILY, LocalDate.MIN, LocalDate.MAX))
                .element(18)
                .extracting("earnings", "balance")
                .containsExactly(14.51, 1175.27);
    }

    @Test
    public void test_forecast_daily_with_deposit_and_withdrawal_success() {
        TEST_PLAN.setStartDate(LocalDate.of(2022, 1, 1));
        TEST_PLAN.setEndDate(LocalDate.of(2022, 7, 1));
        TEST_PLAN.setProfitTarget(1.25);
        TEST_PLAN.setAggregateInterval(AggregateInterval.DAILY);
        TEST_PLAN.setDepositPlan(generateDepositPlan());
        TEST_PLAN.setWithdrawalPlan(generateWithdrawalPlan());

        assertThat(this.tradingPlanService.forecast(TEST_PLAN, AggregateInterval.DAILY, LocalDate.MIN, LocalDate.MAX))
                .element(61)
                .extracting("earnings", "balance")
                .containsExactly(28.05, 2272.16);
    }

    @Test
    public void test_forecast_monthly_success() {
        TEST_PLAN.setStartDate(LocalDate.of(2022, 1, 1));
        TEST_PLAN.setEndDate(LocalDate.of(2022, 7, 1));
        TEST_PLAN.setProfitTarget(11.38);
        TEST_PLAN.setAggregateInterval(AggregateInterval.MONTHLY);

        assertThat(this.tradingPlanService.forecast(TEST_PLAN, AggregateInterval.MONTHLY, LocalDate.MIN, LocalDate.MAX))
                .element(2)
                .extracting("earnings", "balance")
                .containsExactly(141.17, 1381.72);
    }

    @Test
    public void test_forecast_aggregated_success() {
        TEST_PLAN.setStartDate(LocalDate.of(2022, 8, 1));
        TEST_PLAN.setEndDate(LocalDate.of(2023, 8, 1));
        TEST_PLAN.setProfitTarget(1.25);
        TEST_PLAN.setAggregateInterval(AggregateInterval.DAILY);
        TEST_PLAN.setDepositPlan(generateDepositPlan());
        TEST_PLAN.setWithdrawalPlan(null);

        assertThat(this.tradingPlanService.forecast(TEST_PLAN, AggregateInterval.MONTHLY, LocalDate.MIN, LocalDate.MAX))
                .isNotEmpty()
                .element(7)
                .extracting("earnings", "balance")
                .containsExactly(4249.78, 17099.89);
    }
    

    //  ----------------- createTradingPlan -----------------

    @Test
    public void test_createTradingPlan_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.tradingPlanService.createTradingPlan(null))
                .withMessage("The required data for creating a TradingPlan was null or empty");
    }

    @Test
    public void test_createTradingPlan_erroneousCreation() {
        Map<String, Object> map = Map.of("bad", "input");
        assertThatExceptionOfType(EntityCreationException.class)
                .isThrownBy(() -> this.tradingPlanService.createTradingPlan(map))
                .withMessage("A TradingPlan entity could not be created : Cannot invoke \"java.util.Map.get(Object)\" because \"plan\" is null");
    }

    @Test
    public void test_createTradingPlan_success() {

        Map<String, Object> data =
                Map.of(
                        "plan",
                        Map.of(
                                "status", TradingPlanStatus.IN_PROGRESS,
                                "active", true,
                                "name", TEST_NAME,
                                "startDate", TEST_START,
                                "endDate", TEST_END,
                                "profitTarget", TEST_PROFIT,
                                "aggregateInterval", TEST_FREQUENCY,
                                "startingBalance", 1000.0,
                                "absolute", false
                        ),
                        "depositPlan",
                        Map.of(
                                "amount", 350.00,
                                "frequency", AggregateInterval.MONTHLY,
                                "absolute", true
                        ),
                        "withdrawalPlan",
                        Map.of(
                                "amount", 350.00,
                                "frequency", AggregateInterval.MONTHLY,
                                "absolute", true
                        )
                );

        assertThat(this.tradingPlanService.createTradingPlan(data))
                .isNotNull()
                .extracting("profitTarget", "name", "depositPlan.amount", "withdrawalPlan.amount")
                .containsExactly(TEST_PROFIT, TEST_NAME, 350.0, 120.0);
    }


    //  ----------------- updateTradingPlan -----------------

    @Test
    public void test_updateTradingPlan_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.tradingPlanService.updateTradingPlan(TEST_NAME, TEST_START, TEST_END, null))
                .withMessage("The required data for updating the TradingPlan was null or empty");
    }

    @Test
    public void test_updateTradingPlan_erroneousModification() {
        Map<String, Object> map = Map.of("bad", "input");
        assertThatExceptionOfType(EntityModificationException.class)
                .isThrownBy(() -> this.tradingPlanService.updateTradingPlan(TEST_NAME, TEST_START, TEST_END, map))
                .withMessage("An error occurred while modifying the TradingPlan : Cannot invoke \"java.util.Map.get(Object)\" because \"plan\" is null");
    }

    @Test
    public void test_updateTradingPlan_success() {

        Map<String, Object> data =
                Map.of(
                        "plan",
                        Map.of(
                                "status", TradingPlanStatus.COMPLETED,
                                "active", true,
                                "name", "updated name",
                                "startDate", LocalDate.of(2024, 2 ,2),
                                "endDate", LocalDate.of(2025, 3, 3),
                                "profitTarget", 123.0,
                                "aggregateInterval", AggregateInterval.MONTHLY,
                                "startingBalance", 1500.0,
                                "absolute", false
                        ),
                        "depositPlan",
                        Map.of(
                                "amount", 400.0,
                                "frequency", AggregateInterval.YEARLY,
                                "absolute", true
                        ),
                        "withdrawalPlan",
                        Map.of(
                                "amount", 100.0,
                                "frequency", AggregateInterval.YEARLY,
                                "absolute", true
                        )
                );

        assertThat(this.tradingPlanService.updateTradingPlan(TEST_NAME, TEST_START, TEST_END, data))
                .isNotNull()
                .extracting("status", "name", "startDate", "endDate", "profitTarget", "aggregateInterval", "startingBalance", "depositPlan.amount", "withdrawalPlan.amount")
                .containsExactly(TradingPlanStatus.COMPLETED, "updated name", LocalDate.of(2024, 2, 2), LocalDate.of(2025, 3, 3), 123.0, AggregateInterval.MONTHLY, 1500.0, 400.0, 100.0);
    }
}
