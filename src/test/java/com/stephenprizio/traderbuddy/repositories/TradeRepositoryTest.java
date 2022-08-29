package com.stephenprizio.traderbuddy.repositories;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.TradeType;
import com.stephenprizio.traderbuddy.models.entities.Trade;
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
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link TradeRepository}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TradeRepositoryTest extends AbstractGenericTest {

    private static final LocalDateTime TEST1 = LocalDate.of(2022, 8, 24).atStartOfDay();
    private static final LocalDateTime TEST2 = LocalDate.of(2022, 8, 25).atStartOfDay();

    private final Trade TEST_TRADE_1 = generateTestBuyTrade();
    private final Trade TEST_TRADE_2 = generateTestSellTrade();

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TradeRepository tradeRepository;

    @Before
    public void setUp() {
        this.entityManager.persist(TEST_TRADE_1);
        this.entityManager.persist(TEST_TRADE_2);
        this.entityManager.flush();
    }


    //  ----------------- findAllByTradeType -----------------

    @Test
    public void test_findAllByTradeType_success() {
        assertThat(this.tradeRepository.findAllByTradeType(TradeType.BUY))
                .hasSize(1)
                .extracting("openPrice", "closePrice", "netProfit")
                .containsExactly(Tuple.tuple(13083.41, 13098.67, 14.85));
    }


    //  ----------------- findAllTradesWithinDate -----------------

    @Test
    public void test_findAllTradesWithinDate_success() {
        assertThat(this.tradeRepository.findAllTradesWithinDate(TEST1, TEST2))
                .hasSize(2)
                .extracting("openPrice", "closePrice", "netProfit")
                .contains(Tuple.tuple(13083.41, 13098.67, 14.85), Tuple.tuple(13160.09, 13156.12, -4.50));
    }

    @Test
    public void test_findAllTradesWithinDate_restrictiveDate_success() {
        assertThat(this.tradeRepository.findAllTradesWithinDate(TEST1, LocalDateTime.of(2022, 8, 24, 11, 0, 0)))
                .hasSize(1)
                .extracting("openPrice", "closePrice", "netProfit")
                .contains(Tuple.tuple(13160.09, 13156.12, -4.50));
    }


    //  ----------------- findTradeByTradeId -----------------

    @Test
    public void test_findTradeByTradeId_failure() {
        assertThat(this.tradeRepository.findTradeByTradeId("empty"))
                .isNull();
    }

    @Test
    public void test_findTradeByTradeId_success() {
        assertThat(this.tradeRepository.findTradeByTradeId("testId1"))
                .isNotNull()
                .extracting("openPrice", "closePrice", "netProfit")
                .contains(13083.41, 13098.67, 14.85);
    }
}
