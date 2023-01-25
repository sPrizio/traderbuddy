package com.traderbuddyv2.core.services.trade;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.trades.TradeType;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import com.traderbuddyv2.core.exceptions.validation.NoResultFoundException;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.security.User;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.repositories.trade.TradeRepository;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import com.traderbuddyv2.integration.models.dto.eod.IntradayHistoricalDataDTO;
import com.traderbuddyv2.integration.services.eod.EODIntegrationService;
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;

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

    @Autowired
    private TradeService tradeService;

    @MockBean
    private EODIntegrationService eodIntegrationService;

    @MockBean
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @Before
    public void setUp() {

        final Account testAccount = generateTestAccount();
        final User testUser = generateTestUser();
        testUser.setAccount(testAccount);

        Mockito.when(this.tradeRepository.findAllByTradeTypeAndAccountOrderByTradeOpenTimeAsc(TradeType.BUY, testAccount)).thenReturn(List.of(TEST_TRADE_1));
        Mockito.when(this.tradeRepository.findAllTradesWithinDate(TEST1, TEST1.plusYears(1).toLocalDate().atStartOfDay(), testAccount)).thenReturn(List.of(TEST_TRADE_1, TEST_TRADE_2));
        Mockito.when(this.tradeRepository.findTradeByTradeIdAndAccount("testId1", testAccount)).thenReturn(TEST_TRADE_1);
        Mockito.when(this.tradeRepository.findAllByProcessedAndAccountAndRelevantIsTrueOrderByTradeOpenTimeAsc(false, testAccount)).thenReturn(List.of(TEST_TRADE_1));
        Mockito.when(this.traderBuddyUserDetailsService.getCurrentUser()).thenReturn(testUser);
        Mockito.when(this.tradeRepository.findAllRelevantTradesWithinDate(any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(TEST_TRADE_1, TEST_TRADE_2)));
        Mockito.when(this.tradeRepository.findAllTradesWithinDate(any(), any(), any(), any())).thenReturn(new PageImpl<>(List.of(TEST_TRADE_1, TEST_TRADE_2)));
        Mockito.when(this.tradeRepository.findAllTradesForTradeRecord(any(), any(), any())).thenReturn(List.of(TEST_TRADE_1, TEST_TRADE_2));
        Mockito.when(this.eodIntegrationService.getIntradayData(anyString(), anyString(), any(), any())).thenReturn(new IntradayHistoricalDataDTO());
        Mockito.when(this.tradeRepository.findTradeByTradeIdAndAccount("123", testAccount)).thenReturn(generateTestBuyTrade());
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


    //  ----------------- findAllTradesWithinTimespan -----------------

    @Test
    public void test_findAllTradesWithinTimespan_missingParamStart() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesWithinTimespan(null, LocalDateTime.MAX, true))
                .withMessage(CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
    }

    @Test
    public void test_findAllTradesWithinTimespan_missingParamEnd() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesWithinTimespan(LocalDateTime.MAX, null, true))
                .withMessage(CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    public void test_findAllTradesWithinTimespan_invalidInterval() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesWithinTimespan(LocalDateTime.MAX, LocalDateTime.MIN, true))
                .withMessage(CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    public void test_findAllTradesWithinTimespan_success() {
        assertThat(this.tradeService.findAllTradesWithinTimespan(TEST1, TEST2, true))
                .hasSize(2)
                .extracting("openPrice", "closePrice", "netProfit")
                .contains(Tuple.tuple(13083.41, 13098.67, 14.85), Tuple.tuple(13160.09, 13156.12, -4.50));
    }

    @Test
    public void test_findAllTradesWithinTimespan_success_empty() {
        TEST_TRADE_1.setRelevant(false);
        TEST_TRADE_2.setRelevant(false);
        assertThat(this.tradeService.findAllTradesWithinTimespan(TEST1, TEST2, false))
                .isEmpty();
    }


    //  ----------------- findAllTradesWithinTimespan (paged) -----------------

    @Test
    public void test_findAllTradesWithinTimespan_paged_missingParamStart() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesWithinTimespan(null, LocalDateTime.MAX, true, 0, 10))
                .withMessage(CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
    }

    @Test
    public void test_findAllTradesWithinTimespan_paged_missingParamEnd() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesWithinTimespan(LocalDateTime.MAX, null, true, 0, 10))
                .withMessage(CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    public void test_findAllTradesWithinTimespan_paged_invalidInterval() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesWithinTimespan(LocalDateTime.MAX, LocalDateTime.MIN, true, 0, 10))
                .withMessage(CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    public void test_findAllTradesWithinTimespan_paged_success() {
        assertThat(this.tradeService.findAllTradesWithinTimespan(TEST1, TEST2, true, 0, 10))
                .hasSize(2)
                .extracting("openPrice", "closePrice", "netProfit")
                .contains(Tuple.tuple(13083.41, 13098.67, 14.85), Tuple.tuple(13160.09, 13156.12, -4.50));
    }


    //  ----------------- findAllTradesForTradeRecord -----------------

    @Test
    public void test_findAllTradesForTradeRecord_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeService.findAllTradesForTradeRecord(null))
                .withMessage(CoreConstants.Validation.TRADE_RECORD_CANNOT_BE_NULL);
    }

    @Test
    public void test_findAllTradesForTradeRecord_success() {
        assertThat(this.tradeService.findAllTradesForTradeRecord(generateTestTradeRecord()))
                .isNotEmpty();
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


    //  ----------------- findTradesByProcessed -----------------

    @Test
    public void test_findTradesByProcessed_success() {
        assertThat(this.tradeService.findTradesByProcessed(false))
                .hasSize(1);
    }


    //  ----------------- findTradeRecap -----------------

    @Test
    public void test_findTradeRecap_badId() {
        assertThatExceptionOfType(NoResultFoundException.class)
                .isThrownBy(() -> this.tradeService.findTradeRecap("BAD"))
                .withMessage("No trade was found with trade id: BAD");
    }

    @Test
    public void test_findTradeRecap_success() {
        assertThat(this.tradeService.findTradeRecap("123"))
                .extracting("points")
                .isNotNull();
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
