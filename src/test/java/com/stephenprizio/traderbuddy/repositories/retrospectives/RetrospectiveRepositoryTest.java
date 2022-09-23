package com.stephenprizio.traderbuddy.repositories.retrospectives;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.AggregateInterval;
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
 * Testing class for {@link RetrospectiveRepository}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RetrospectiveRepositoryTest extends AbstractGenericTest {

    @Autowired
    private RetrospectiveRepository retrospectiveRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Before
    public void setUp() {
        generateRetrospectives().forEach(r -> this.entityManager.persist(r));
        this.entityManager.flush();
    }


    //  ----------------- findAllRetrospectivesWithinDate -----------------

    @Test
    public void test_findAllRetrospectivesWithinDate_success() {
        assertThat(this.retrospectiveRepository.findAllRetrospectivesWithinDate(LocalDate.of(2022, 1, 1), LocalDate.of(2022, 12, 1), AggregateInterval.MONTHLY))
                .hasSize(1);
    }


    //  ----------------- findRetrospectiveByStartDateAndEndDateAndIntervalFrequency -----------------

    @Test
    public void test_findRetrospectiveByStartDateAndEndDateAndIntervalFrequency_success() {
        assertThat(this.retrospectiveRepository.findRetrospectiveByStartDateAndEndDateAndIntervalFrequency(LocalDate.of(2022, 9, 5), LocalDate.of(2022, 9, 11), AggregateInterval.MONTHLY))
                .isNotNull();
    }
}
