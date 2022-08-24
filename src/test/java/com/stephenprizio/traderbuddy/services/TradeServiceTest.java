package com.stephenprizio.traderbuddy.services;

import com.stephenprizio.traderbuddy.enums.TradeType;
import com.stephenprizio.traderbuddy.exceptions.IllegalParameterException;
import com.stephenprizio.traderbuddy.models.entities.Trade;
import com.stephenprizio.traderbuddy.repositories.TradeRepository;
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
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link TradeService}
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TradeServiceTest {

    private static final LocalDateTime TEST1 = LocalDate.of(2022, 8, 24).atStartOfDay();
    private static final LocalDateTime TEST2 = LocalDate.of(2022, 8, 25).atStartOfDay();

    @Autowired
    private TradeService tradeService;

    @MockBean
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

        Mockito.when(this.tradeRepository.findAllByTradeType(TradeType.BUY)).thenReturn(List.of(trade1));
        Mockito.when(this.tradeRepository.findAllTradesWithinDate(TEST1, TEST2)).thenReturn(List.of(trade1, trade2));
    }


    //  ----------------- findAllByTradeType -----------------

    @Test
    public void test_findAllByTradeType_missingParamTradeType() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllByTradeType(null))
                .withMessage("tradeType cannot be null");
    }

    @Test
    public void test_findAllByTradeType_success() {
        assertThat(this.tradeService.findAllByTradeType(TradeType.BUY))
                .hasSize(1)
                .extracting("openPrice", "closePrice", "netProfit")
                .containsExactly(Tuple.tuple(13083.41, 13098.67, 14.85));
    }


    //  ----------------- findAllTradesWithinDate -----------------

    @Test
    public void test_findAllTradesWithinDate_missingParamStart() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesWithinDate(null, LocalDateTime.now()))
                .withMessage("startDate cannot be null");
    }

    @Test
    public void test_findAllTradesWithinDate_missingParamEnd() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesWithinDate(LocalDateTime.now(), null))
                .withMessage("endDate cannot be null");
    }

    @Test
    public void test_findAllTradesWithinDate_success() {
        assertThat(this.tradeService.findAllTradesWithinDate(TEST1, TEST2))
                .hasSize(2)
                .extracting("openPrice", "closePrice", "netProfit")
                .contains(Tuple.tuple(13083.41, 13098.67, 14.85), Tuple.tuple(13160.09, 13156.12, -4.50));
    }
}
