package com.stephenprizio.traderbuddy.repositories.plans;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.plans.TradingPlanStatus;
import com.stephenprizio.traderbuddy.models.entities.plans.TradingPlan;
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
 * Testing class for {@link TradingPlanRepository}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TradingPlanRepositoryTest extends AbstractGenericTest {

    private final TradingPlan TEST_TRADING_PLAN_ACTIVE = generateTestTradingPlan();
    private final TradingPlan TEST_TRADING_INACTIVE = generateInactiveTestTradingPlan();

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TradingPlanRepository tradingPlanRepository;

    @Before
    public void setUp() {
        this.entityManager.persist(TEST_TRADING_PLAN_ACTIVE);
        this.entityManager.persist(TEST_TRADING_INACTIVE);
        this.entityManager.flush();
    }


    //  ----------------- findTradingPlanByActiveIsTrue -----------------

    @Test
    public void test_findTradingPlanByActiveIsTrue_success() {
        assertThat(this.tradingPlanRepository.findTradingPlanByActiveIsTrue())
                .hasSize(1)
                .extracting("profitTarget", "name")
                .containsExactly(Tuple.tuple(528491.0, "Test Trading Plan Active"));
    }


    //  ----------------- findAllByStatus -----------------

    @Test
    public void test_findAllByStatus_success() {
        assertThat(this.tradingPlanRepository.findAllByStatus(TradingPlanStatus.IN_PROGRESS))
                .hasSize(1)
                .extracting("profitTarget", "name")
                .containsExactly(Tuple.tuple(528491.0, "Test Trading Plan Active"));
    }


    //  ----------------- findTradingPlanByNameAndStartDateAndEndDate -----------------

    @Test
    public void test_findTradingPlanByNameAndStartDateAndEndDate_success() {
        assertThat(this.tradingPlanRepository.findTradingPlanByNameAndStartDateAndEndDate("Test Trading Plan Active", LocalDate.of(2022, 1, 1), LocalDate.of(2025, 1, 1)))
                .isNotNull()
                .extracting("profitTarget", "name")
                .containsExactly(528491.0, "Test Trading Plan Active");
    }


    //  ----------------- resetTradingPlans -----------------

    @Test
    public void test_resetTradingPlans_success() {
        assertThat(this.tradingPlanRepository.resetTradingPlans())
                .isNotNull()
                .isEqualTo(1);
    }
}
