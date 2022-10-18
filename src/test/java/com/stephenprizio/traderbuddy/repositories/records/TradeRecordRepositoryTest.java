package com.stephenprizio.traderbuddy.repositories.records;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import com.stephenprizio.traderbuddy.models.entities.records.TradeRecord;
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
 * Testing class for {@link TradeRecordRepository}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TradeRecordRepositoryTest extends AbstractGenericTest {

    private final TradeRecord TEST = generateTestTradeRecord();

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TradeRecordRepository tradeRecordRepository;

    @Before
    public void setUp() {
        this.entityManager.persist(TEST);
        this.entityManager.flush();
    }


    //  ----------------- findTradingRecordByStartDateAndEndDateAndAggregateInterval -----------------

    @Test
    public void test_findTradingRecordByStartDateAndEndDateAndAggregateInterval_success() {
        assertThat(this.tradeRecordRepository.findTradingRecordByStartDateAndEndDateAndAggregateInterval(LocalDate.of(2022, 10, 17), LocalDate.of(2022, 10, 18), AggregateInterval.DAILY))
                .isNotNull()
                .extracting("netProfit", "balance")
                .containsExactly(624.23, 3896.23);
    }
}
