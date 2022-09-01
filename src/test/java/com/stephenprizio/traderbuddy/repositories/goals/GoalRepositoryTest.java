package com.stephenprizio.traderbuddy.repositories.goals;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.goals.GoalStatus;
import com.stephenprizio.traderbuddy.models.entities.goals.Goal;
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link GoalRepository}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class GoalRepositoryTest extends AbstractGenericTest {

    private final Goal TEST_GOAL_ACTIVE = generateTestGoal();
    private final Goal TEST_GOAL_INACTIVE = generateInactiveTestGoal();

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GoalRepository goalRepository;

    @Before
    public void setUp() {
        this.entityManager.persist(TEST_GOAL_ACTIVE);
        this.entityManager.persist(TEST_GOAL_INACTIVE);
        this.entityManager.flush();
    }


    //  ----------------- findGoalByActiveIsTrue -----------------

    @Test
    public void test_findGoalByActiveIsTrue_success() {
        assertThat(this.goalRepository.findGoalByActiveIsTrue())
                .hasSize(1)
                .extracting("profitTarget", "name")
                .containsExactly(Tuple.tuple(528491.0, "Test Goal Active"));
    }


    //  ----------------- findAllByStatus -----------------

    @Test
    public void test_findAllByStatus_success() {
        assertThat(this.goalRepository.findAllByStatus(GoalStatus.IN_PROGRESS))
                .hasSize(1)
                .extracting("profitTarget", "name")
                .containsExactly(Tuple.tuple(528491.0, "Test Goal Active"));
    }


    //  ----------------- findGoalByNameAndStartDateAndEndDate -----------------

    @Test
    public void test_findGoalByNameAndStartDateAndEndDate_success() {
        assertThat(this.goalRepository.findGoalByNameAndStartDateAndEndDate("Test Goal Active", LocalDate.of(2022, 1, 1), LocalDate.of(2025, 1, 1)))
                .isNotNull()
                .extracting("profitTarget", "name")
                .containsExactly(528491.0, "Test Goal Active");
    }


    //  ----------------- resetGoals -----------------

    @Test
    public void test_resetGoals_success() {
        assertThat(this.goalRepository.resetGoals())
                .isNotNull()
                .isEqualTo(1);
    }
}
