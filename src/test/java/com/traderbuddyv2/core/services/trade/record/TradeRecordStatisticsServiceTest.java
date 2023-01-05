package com.traderbuddyv2.core.services.trade.record;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link TradeRecordStatisticsService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TradeRecordStatisticsServiceTest extends AbstractGenericTest {

    @MockBean
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @Resource
    private TradeRecordStatisticsService tradeRecordStatisticsService;

    @Before
    public void setUp() {
        Mockito.when(this.traderBuddyUserDetailsService.getCurrentUser()).thenReturn(generateTestUser());
    }

    //  ----------------- generateStatistics -----------------

    @Test
    public void test_generateStatistics_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordStatisticsService.generateStatistics(null, List.of()))
                .withMessage(CoreConstants.Validation.TRADE_RECORD_CANNOT_BE_NULL);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordStatisticsService.generateStatistics(generateTestTradeRecord(), null))
                .withMessage("trades cannot be null");
    }

    @Test
    public void test_generateStatistics_success() {
        assertThat(this.tradeRecordStatisticsService.generateStatistics(generateTestTradeRecord(), List.of(generateTestBuyTrade(), generateTestSellTrade())))
                .isNotNull()
                .extracting("numberOfWinningTrades", "winPercentage", "netProfit", "averageWinAmount")
                .containsExactly(31, 58, 194.40, 13.6);
    }

    @Test
    public void test_generateStatistics_success_no_losses() {
        assertThat(this.tradeRecordStatisticsService.generateStatistics(generateTestTradeRecord(), List.of(generateTestBuyTrade())))
                .isNotNull()
                .extracting("numberOfWinningTrades", "winPercentage", "netProfit", "largestLossSize")
                .containsExactly(31, 60, 198.9, 2.1);
    }

    @Test
    public void test_generateStatistics_success_no_wins() {
        assertThat(this.tradeRecordStatisticsService.generateStatistics(generateTestTradeRecord(), List.of(generateTestSellTrade())))
                .isNotNull()
                .extracting("numberOfWinningTrades", "winPercentage", "netProfit", "largestWinSize")
                .containsExactly(30, 58, 179.55, 2.25);
    }
}
