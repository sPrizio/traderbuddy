package com.stephenprizio.traderbuddy.services.summary;

import com.stephenprizio.traderbuddy.AbstractTraderBuddyTest;
import com.stephenprizio.traderbuddy.exceptions.validation.IllegalParameterException;
import com.stephenprizio.traderbuddy.models.entities.Trade;
import com.stephenprizio.traderbuddy.services.trades.TradeService;
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
 * Testing class for {@link TradesSummaryService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TradesSummaryServiceTest extends AbstractTraderBuddyTest {

    private static final LocalDateTime TEST_DAY1 = LocalDate.of(2022, 8, 24).atStartOfDay();
    private static final LocalDateTime TEST_DAY2 = LocalDate.of(2022, 8, 25).atStartOfDay();

    private final Trade TEST_TRADE_1 = generateTestBuyTrade();
    private final Trade TEST_TRADE_2 = generateTestSellTrade();

    @Autowired
    private TradesSummaryService tradesSummaryService;

    @MockBean
    private TradeService tradeService;

    @Before
    public void setUp() {
        Mockito.when(this.tradeService.findAllTradesWithinDate(TEST_DAY1, TEST_DAY2)).thenReturn(List.of(TEST_TRADE_1, TEST_TRADE_2));
    }


    //  ----------------- getSummaryForTimeSpan -----------------

    @Test
    public void test_getSummaryForTimeSpan_missingParamStart() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradesSummaryService.getSummaryForTimeSpan(null, LocalDateTime.MAX))
                .withMessage("startDate cannot be null");
    }

    @Test
    public void test_getSummaryForTimeSpan_missingParamEnd() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradesSummaryService.getSummaryForTimeSpan(LocalDateTime.MAX, null))
                .withMessage("endDate cannot be null");
    }

    @Test
    public void test_getSummaryForTimeSpan_invalidInterval() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradesSummaryService.getSummaryForTimeSpan(LocalDateTime.MAX, LocalDateTime.MIN))
                .withMessage("startDate was after endDate or vice versa");
    }

    @Test
    public void test_getSummaryForTimeSpan_success() {
        assertThat(this.tradesSummaryService.getSummaryForTimeSpan(TEST_DAY1, TEST_DAY2))
                .isNotNull()
                .extracting("numberOfTrades", "winPercentage", "netProfit")
                .containsExactly(2, 50, 10.35);
    }
}
