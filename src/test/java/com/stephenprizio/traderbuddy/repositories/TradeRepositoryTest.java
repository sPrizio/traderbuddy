package com.stephenprizio.traderbuddy.repositories;

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
public class TradeRepositoryTest {

    private static final LocalDateTime TEST1 = LocalDate.of(2022, 8, 24).atStartOfDay();
    private static final LocalDateTime TEST2 = LocalDate.of(2022, 8, 25).atStartOfDay();

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TradeRepository tradeRepository;

    @Before
    public void setUp() {

        Trade trade1 = new Trade();
        Trade trade2 = new Trade();

        trade1.setResultOfTrade("Winner winner chicken dinner");
        trade1.setTradeType(TradeType.BUY);
        trade1.setClosePrice(13098.67);
        trade1.setTradeCloseTime(LocalDateTime.of(2022, 8, 24, 11, 37, 24));
        trade1.setTradeOpenTime(LocalDateTime.of(2022, 8, 24, 11, 32, 58));
        trade1.setLotSize(0.75);
        trade1.setNetProfit(14.85);
        trade1.setOpenPrice(13083.41);
        trade1.setReasonForEntrance("I have my reasons");

        trade2.setResultOfTrade("Loser like a real loser");
        trade2.setTradeType(TradeType.SELL);
        trade2.setClosePrice(13156.12);
        trade2.setTradeCloseTime(LocalDateTime.of(2022, 8, 24, 10, 24, 36));
        trade2.setTradeOpenTime(LocalDateTime.of(2022, 8, 24, 10, 25, 12));
        trade2.setLotSize(0.75);
        trade2.setNetProfit(-4.50);
        trade2.setOpenPrice(13160.09);
        trade2.setReasonForEntrance("I continue to have my reasons");

        this.entityManager.persist(trade1);
        this.entityManager.persist(trade2);
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
}
