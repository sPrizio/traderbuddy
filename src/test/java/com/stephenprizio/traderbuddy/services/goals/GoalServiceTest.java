package com.stephenprizio.traderbuddy.services.goals;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.enums.goals.GoalStatus;
import com.stephenprizio.traderbuddy.exceptions.system.EntityCreationException;
import com.stephenprizio.traderbuddy.exceptions.system.EntityModificationException;
import com.stephenprizio.traderbuddy.exceptions.validation.IllegalParameterException;
import com.stephenprizio.traderbuddy.exceptions.validation.MissingRequiredDataException;
import com.stephenprizio.traderbuddy.models.entities.goals.Goal;
import com.stephenprizio.traderbuddy.repositories.goals.GoalRepository;
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
 * Testing class for {@link GoalService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GoalServiceTest extends AbstractGenericTest {

    private static final String TEST_NAME = "Test Goal Active";
    private static final LocalDate TEST_START = LocalDate.of(2022, 1, 1);
    private static final LocalDate TEST_END = LocalDate.of(2025, 1, 1);
    private static final Double TEST_PROFIT = 528491.0;
    private static final CompoundFrequency TEST_FREQUENCY = CompoundFrequency.DAILY;

    private final Goal TEST_GOAL_ACTIVE = generateTestGoal();

    @MockBean
    private GoalRepository goalRepository;

    @Autowired
    private GoalService goalService;

    @Before
    public void setUp() {
        Mockito.when(this.goalRepository.findGoalByActiveIsTrue()).thenReturn(List.of(TEST_GOAL_ACTIVE));
        Mockito.when(this.goalRepository.findAllByStatus(GoalStatus.IN_PROGRESS)).thenReturn(List.of(TEST_GOAL_ACTIVE));
        Mockito.when(this.goalRepository.findGoalByNameAndStartDateAndEndDate(TEST_NAME, TEST_START, TEST_END)).thenReturn(TEST_GOAL_ACTIVE);
        Mockito.when(this.goalRepository.save(any())).thenReturn(TEST_GOAL_ACTIVE);
    }


    //  ----------------- findGoalByActiveIsTrue -----------------

    @Test
    public void test_findGoalByActiveIsTrue_success() {
        assertThat(this.goalService.findCurrentlyActiveGoal())
                .isPresent()
                .map(Goal::getProfitTarget)
                .get()
                .isEqualTo(TEST_PROFIT);
    }


    //  ----------------- findGoalsForStatus -----------------

    @Test
    public void test_findGoalsForStatus_missingParamStatus() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.goalService.findGoalsForStatus(null))
                .withMessage("goal status cannot be null");
    }

    @Test
    public void test_findGoalsForStatus_success() {
        assertThat(this.goalService.findGoalsForStatus(GoalStatus.IN_PROGRESS))
                .hasSize(1)
                .extracting("profitTarget", "name")
                .contains(Tuple.tuple(TEST_PROFIT, TEST_NAME));
    }


    //  ----------------- findGoalForNameAndStartDateAndEndDate -----------------

    @Test
    public void test_findGoalForNameAndStartDateAndEndDate_missingParamName() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.goalService.findGoalForNameAndStartDateAndEndDate(null, LocalDate.MIN, LocalDate.MAX))
                .withMessage("name cannot be null");
    }

    @Test
    public void test_findGoalForNameAndStartDateAndEndDate_missingParamStartDate() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.goalService.findGoalForNameAndStartDateAndEndDate("name", null, LocalDate.MAX))
                .withMessage("startDate cannot be null");
    }

    @Test
    public void test_findGoalForNameAndStartDateAndEndDate_missingParamEndDate() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.goalService.findGoalForNameAndStartDateAndEndDate("name", LocalDate.MIN, null))
                .withMessage("endDate cannot be null");
    }

    @Test
    public void test_findGoalForNameAndStartDateAndEndDate_badDates() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.goalService.findGoalForNameAndStartDateAndEndDate("name", LocalDate.MAX, LocalDate.MIN))
                .withMessage("startDate was after endDate or vice versa");
    }

    @Test
    public void test_findGoalForNameAndStartDateAndEndDate_success() {
        assertThat(this.goalService.findGoalForNameAndStartDateAndEndDate(TEST_NAME, TEST_START, TEST_END))
                .isNotNull()
                .get()
                .extracting("profitTarget", "name")
                .containsExactly(TEST_PROFIT, TEST_NAME);
    }


    //  ----------------- createGoal -----------------

    @Test
    public void test_createGoal_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.goalService.createGoal(null))
                .withMessage("The required data for creating a Goal was null or empty");
    }

    @Test
    public void test_createGoal_erroneousCreation() {
        Map<String, Object> map = Map.of("bad", "input");
        assertThatExceptionOfType(EntityCreationException.class)
                .isThrownBy(() -> this.goalService.createGoal(map))
                .withMessage("A Goal entity could not be created : Cannot invoke \"Object.toString()\" because the return value of \"java.util.Map.get(Object)\" is null");
    }

    @Test
    public void test_createGoal_success() {

        Map<String, Object> data =
                Map.of(
                        "status", GoalStatus.IN_PROGRESS,
                        "active", true,
                        "name", TEST_NAME,
                        "startDate", TEST_START,
                        "endDate", TEST_END,
                        "profitTarget", TEST_PROFIT,
                        "compoundFrequency", TEST_FREQUENCY,
                        "startingBalance", 1000.0
                );

        assertThat(this.goalService.createGoal(data))
                .isNotNull()
                .extracting("profitTarget", "name")
                .containsExactly(TEST_PROFIT, TEST_NAME);
    }


    //  ----------------- updateGoal -----------------

    @Test
    public void test_updateGoal_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.goalService.updateGoal(TEST_NAME, TEST_START, TEST_END, null))
                .withMessage("The required data for updating the Goal was null or empty");
    }

    @Test
    public void test_updateGoal_erroneousModification() {
        Map<String, Object> map = Map.of("bad", "input");
        assertThatExceptionOfType(EntityModificationException.class)
                .isThrownBy(() -> this.goalService.updateGoal(TEST_NAME, TEST_START, TEST_END, map))
                .withMessage("An error occurred while modifying the Goal : Cannot invoke \"Object.toString()\" because the return value of \"java.util.Map.get(Object)\" is null");
    }

    @Test
    public void test_updateGoal_success() {

        Map<String, Object> data =
                Map.of(
                        "status", GoalStatus.COMPLETED,
                        "active", true,
                        "name", "updated name",
                        "startDate", LocalDate.of(2024, 2 ,2),
                        "endDate", LocalDate.of(2025, 3, 3),
                        "profitTarget", 123.0,
                        "compoundFrequency", CompoundFrequency.MONTHLY,
                        "startingBalance", 1500.0
                );

        assertThat(this.goalService.updateGoal(TEST_NAME, TEST_START, TEST_END, data))
                .isNotNull()
                .extracting("status", "name", "startDate", "endDate", "profitTarget", "compoundFrequency", "startingBalance")
                .containsExactly(GoalStatus.COMPLETED, "updated name", LocalDate.of(2024, 2, 2), LocalDate.of(2025, 3, 3), 123.0, CompoundFrequency.MONTHLY, 1500.0);
    }
}
