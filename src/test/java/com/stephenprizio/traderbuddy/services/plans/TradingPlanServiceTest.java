package com.stephenprizio.traderbuddy.services.plans;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.enums.plans.TradingPlanStatus;
import com.stephenprizio.traderbuddy.exceptions.system.EntityCreationException;
import com.stephenprizio.traderbuddy.exceptions.system.EntityModificationException;
import com.stephenprizio.traderbuddy.exceptions.validation.IllegalParameterException;
import com.stephenprizio.traderbuddy.exceptions.validation.MissingRequiredDataException;
import com.stephenprizio.traderbuddy.models.entities.plans.TradingPlan;
import com.stephenprizio.traderbuddy.repositories.plans.TradingPlanRepository;
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
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
    private static final Double TEST_PROFIT = 528491.0;
    private static final CompoundFrequency TEST_FREQUENCY = CompoundFrequency.DAILY;

    private final TradingPlan TEST_TRADING_PLAN_ACTIVE = generateTestTradingPlan();

    @MockBean
    private TradingPlanRepository tradingPlanRepository;

    @Autowired
    private TradingPlanService tradingPlanService;

    @Before
    public void setUp() {
        Mockito.when(this.tradingPlanRepository.findTradingPlanByActiveIsTrue()).thenReturn(List.of(TEST_TRADING_PLAN_ACTIVE));
        Mockito.when(this.tradingPlanRepository.findAllByStatus(TradingPlanStatus.IN_PROGRESS)).thenReturn(List.of(TEST_TRADING_PLAN_ACTIVE));
        Mockito.when(this.tradingPlanRepository.findTradingPlanByNameAndStartDateAndEndDate(TEST_NAME, TEST_START, TEST_END)).thenReturn(TEST_TRADING_PLAN_ACTIVE);
        Mockito.when(this.tradingPlanRepository.save(any())).thenReturn(TEST_TRADING_PLAN_ACTIVE);
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
                .withMessage("startDate cannot be null");
    }

    @Test
    public void test_findTradingPlanForNameAndStartDateAndEndDate_missingParamEndDate() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradingPlanService.findTradingPlanForNameAndStartDateAndEndDate("name", LocalDate.MIN, null))
                .withMessage("endDate cannot be null");
    }

    @Test
    public void test_findTradingPlanForNameAndStartDateAndEndDate_badDates() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradingPlanService.findTradingPlanForNameAndStartDateAndEndDate("name", LocalDate.MAX, LocalDate.MIN))
                .withMessage("startDate was after endDate or vice versa");
    }

    @Test
    public void test_findTradingPlanForNameAndStartDateAndEndDate_success() {
        assertThat(this.tradingPlanService.findTradingPlanForNameAndStartDateAndEndDate(TEST_NAME, TEST_START, TEST_END))
                .isNotNull()
                .get()
                .extracting("profitTarget", "name")
                .containsExactly(TEST_PROFIT, TEST_NAME);
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
                                "compoundFrequency", TEST_FREQUENCY,
                                "startingBalance", 1000.0
                        ),
                        "depositPlan",
                        Map.of(
                                "amount", 350.00,
                                "frequency", CompoundFrequency.MONTHLY
                        ),
                        "withdrawalPlan",
                        Map.of(
                                "amount", 350.00,
                                "frequency", CompoundFrequency.MONTHLY
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
                                "compoundFrequency", CompoundFrequency.MONTHLY,
                                "startingBalance", 1500.0
                        ),
                        "depositPlan",
                        Map.of(
                                "amount", 400.0,
                                "frequency", CompoundFrequency.YEARLY
                        ),
                        "withdrawalPlan",
                        Map.of(
                                "amount", 100.0,
                                "frequency", CompoundFrequency.YEARLY
                        )
                );

        assertThat(this.tradingPlanService.updateTradingPlan(TEST_NAME, TEST_START, TEST_END, data))
                .isNotNull()
                .extracting("status", "name", "startDate", "endDate", "profitTarget", "compoundFrequency", "startingBalance", "depositPlan.amount", "withdrawalPlan.amount")
                .containsExactly(TradingPlanStatus.COMPLETED, "updated name", LocalDate.of(2024, 2, 2), LocalDate.of(2025, 3, 3), 123.0, CompoundFrequency.MONTHLY, 1500.0, 400.0, 100.0);
    }
}
