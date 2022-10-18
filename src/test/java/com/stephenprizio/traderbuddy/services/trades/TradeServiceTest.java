package com.stephenprizio.traderbuddy.services.trades;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.trades.TradeType;
import com.stephenprizio.traderbuddy.exceptions.validation.IllegalParameterException;
import com.stephenprizio.traderbuddy.models.entities.trades.Trade;
import com.stephenprizio.traderbuddy.repositories.trades.TradeRepository;
import com.stephenprizio.traderbuddy.services.summary.TradingSummaryService;
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link TradeService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TradeServiceTest extends AbstractGenericTest {

    private static final LocalDateTime TEST1 = LocalDate.of(2022, 8, 24).atStartOfDay();
    private static final LocalDateTime TEST2 = LocalDate.of(2022, 8, 25).atStartOfDay();

    private final Trade TEST_TRADE_1 = generateTestBuyTrade();
    private final Trade TEST_TRADE_2 = generateTestSellTrade();

    @MockBean
    private TradeRepository tradeRepository;

    @MockBean
    private TradingSummaryService tradingSummaryService;

    @Autowired
    private TradeService tradeService;

    @Before
    public void setUp() {
        Mockito.when(this.tradeRepository.findAllByTradeTypeOrderByTradeOpenTimeAsc(TradeType.BUY)).thenReturn(List.of(TEST_TRADE_1));
        Mockito.when(this.tradeRepository.findAllTradesWithinDate(TEST1, TEST2)).thenReturn(List.of(TEST_TRADE_1, TEST_TRADE_2));
        Mockito.when(this.tradeRepository.findTradeByTradeId("testId1")).thenReturn(TEST_TRADE_1);
        Mockito.when(this.tradeRepository.findAllByProcessed(false)).thenReturn(List.of(TEST_TRADE_1));
        Mockito.when(this.tradingSummaryService.getReportOfSummariesForTimeSpan(any(), any(), any())).thenReturn(generateTradingSummary());
    }


    //  ----------------- findAllByTradeType -----------------

    @Test
    public void test_findAllByTradeType_missingParamTradeType() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllByTradeType(null, true))
                .withMessage("tradeType cannot be null");
    }

    @Test
    public void test_findAllByTradeType_success() {
        assertThat(this.tradeService.findAllByTradeType(TradeType.BUY, true))
                .hasSize(1)
                .extracting("openPrice", "closePrice", "netProfit")
                .containsExactly(Tuple.tuple(13083.41, 13098.67, 14.85));
    }

    @Test
    public void test_findAllByTradeType_success_empty() {
        TEST_TRADE_1.setRelevant(false);
        assertThat(this.tradeService.findAllByTradeType(TradeType.BUY, false))
                .isEmpty();
    }


    //  ----------------- findAllTradesWithinDate -----------------

    @Test
    public void test_findAllTradesWithinDate_missingParamStart() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesWithinDate(null, LocalDateTime.MAX, true))
                .withMessage("startDate cannot be null");
    }

    @Test
    public void test_findAllTradesWithinDate_missingParamEnd() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesWithinDate(LocalDateTime.MAX, null, true))
                .withMessage("endDate cannot be null");
    }

    @Test
    public void test_findAllTradesWithinDate_invalidInterval() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesWithinDate(LocalDateTime.MAX, LocalDateTime.MIN, true))
                .withMessage("startDate was after endDate or vice versa");
    }

    @Test
    public void test_findAllTradesWithinDate_success() {
        assertThat(this.tradeService.findAllTradesWithinDate(TEST1, TEST2, true))
                .hasSize(2)
                .extracting("openPrice", "closePrice", "netProfit")
                .contains(Tuple.tuple(13083.41, 13098.67, 14.85), Tuple.tuple(13160.09, 13156.12, -4.50));
    }

    @Test
    public void test_findAllTradesWithinDate_success_empty() {
        TEST_TRADE_1.setRelevant(false);
        TEST_TRADE_2.setRelevant(false);
        assertThat(this.tradeService.findAllTradesWithinDate(TEST1, TEST2, false))
                .isEmpty();
    }


    //  ----------------- findTradeByTradeId -----------------

    @Test
    public void test_findTradeByTradeId_missingParamStart() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findTradeByTradeId(null))
                .withMessage("tradeId cannot be null");
    }

    @Test
    public void test_findTradeByTradeId_success() {
        assertThat(this.tradeService.findTradeByTradeId("testId1"))
                .map(Trade::getTradeId)
                .hasValue("testId1");
    }


    //  ----------------- findRecentTrades -----------------

    @Test
    public void test_findRecentTrades_success_all_results() {
        assertThat(this.tradeService.findRecentTrades(-1).get(0).getNetProfit())
                .isEqualTo(10.35);
    }

    @Test
    public void test_findRecentTrades_success_limited_results() {
        assertThat(this.tradeService.findRecentTrades(1).get(0).getNetProfit())
                .isEqualTo(10.35);
    }


    //  ----------------- findTradesByProcessed -----------------

    @Test
    public void test_findTradesByProcessed_success() {
        assertThat(this.tradeService.findTradesByProcessed(false))
                .hasSize(1);
    }


    //  ----------------- disregardTrade -----------------

    @Test
    public void test_disregardTrade_missingParamTradeId() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.disregardTrade(null))
                .withMessage("tradeId cannot be null");
    }

    @Test
    public void test_disregardTrade_failure() {
        assertThat(this.tradeService.disregardTrade("badId"))
                .isFalse();
    }

    @Test
    public void test_disregardTrade_success() {
        assertThat(this.tradeService.disregardTrade("testId1"))
                .isTrue();
    }
}
