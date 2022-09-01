package com.stephenprizio.traderbuddy.services.goals;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.goals.GoalStatus;
import com.stephenprizio.traderbuddy.exceptions.validation.IllegalParameterException;
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

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link GoalService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GoalServiceTest extends AbstractGenericTest {

    private final Goal TEST_GOAL_ACTIVE = generateTestGoal();
    private final Goal TEST_GOAL_INACTIVE = generateInactiveTestGoal();

    @MockBean
    private GoalRepository goalRepository;

    @Autowired
    private GoalService goalService;

    @Before
    public void setUp() {
        Mockito.when(this.goalRepository.findGoalByActiveIsTrue()).thenReturn(List.of(TEST_GOAL_ACTIVE));
        Mockito.when(this.goalRepository.findAllByStatus(GoalStatus.IN_PROGRESS)).thenReturn(List.of(TEST_GOAL_ACTIVE));
    }


    //  ----------------- findGoalByActiveIsTrue -----------------

    @Test
    public void test_findGoalByActiveIsTrue_success() {
        assertThat(this.goalService.findCurrentlyActiveGoal())
                .isPresent()
                .map(Goal::getProfitTarget)
                .get()
                .isEqualTo(528491.0);
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
                .contains(Tuple.tuple(528491.0, "Test Goal Active"));
    }
}
