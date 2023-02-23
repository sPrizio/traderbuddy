package com.traderbuddyv2.core.services.analysis;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.analysis.AnalysisSort;
import com.traderbuddyv2.core.enums.analysis.AnalysisTimeBucket;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import com.traderbuddyv2.core.services.trade.TradeService;
import com.traderbuddyv2.core.services.trade.record.TradeRecordService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;

/**
 * Testing class for {@link AnalysisService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AnalysisServiceTest extends AbstractGenericTest {

    @MockBean
    private TradeService tradeService;

    @MockBean
    private TradeRecordService tradeRecordService;

    @Autowired
    private AnalysisService analysisService;

    @Before
    public void setUp() {
        Mockito.when(this.tradeService.findAllTradesWithinTimespan(any(), any(), anyBoolean())).thenReturn(List.of(generateTestBuyTrade(), generateTestSellTrade()));
        Mockito.when(this.tradeRecordService.computeTradingRate(any(), any(), any())).thenReturn(1.0);
        Mockito.when(this.tradeRecordService.findHistory(any(), any(), any())).thenReturn(List.of(generateTestTradeRecord()));
    }


    //  ----------------- getTopTradePerformance -----------------

    @Test
    public void test_getTopTradePerformance_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.analysisService.getTopTradePerformance(null, LocalDate.MAX, AnalysisSort.PIPS, false, 10))
                .withMessage(CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.analysisService.getTopTradePerformance(LocalDate.MIN, null, AnalysisSort.PIPS, false, 10))
                .withMessage(CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.analysisService.getTopTradePerformance(LocalDate.MIN, LocalDate.MAX, null, false, 10))
                .withMessage("sort cannot be null");
    }

    @Test
    public void test_getTopTradePerformance_success() {
        assertThat(this.analysisService.getTopTradePerformance(LocalDate.MIN, LocalDate.MAX, AnalysisSort.PIPS, false, 10))
                .isNotEmpty();
    }


    //  ----------------- getAverageTradePerformance -----------------

    @Test
    public void test_getAverageTradePerformance_success() {
        assertThat(this.analysisService.getAverageTradePerformance(LocalDate.MIN, LocalDate.MAX, true, 10))
                .isNotNull();
    }


    //  ----------------- getTradeBuckets -----------------

    @Test
    public void test_getTradeBuckets_success() {
        assertThat(this.analysisService.getTradeBuckets(LocalDate.MIN, LocalDate.MAX, AnalysisTimeBucket.FIVE_MINUTES))
                .isNotNull();
    }


    //  ----------------- getWinningDaysBreakdown -----------------

    @Test
    public void test_getWinningDaysBreakdown_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.analysisService.getWinningDaysBreakdown(null, LocalDate.MAX, 10, false))
                .withMessage(CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.analysisService.getWinningDaysBreakdown(LocalDate.MIN, null, 10, false))
                .withMessage(CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    public void test_getWinningDaysBreakdown_success() {
        assertThat(this.analysisService.getWinningDaysBreakdown(LocalDate.MIN, LocalDate.MAX, 50, false).buckets())
                .size()
                .isEqualTo(4);
    }


    //  ----------------- getIrrelevantTrades -----------------

    @Test
    public void test_getIrrelevantTrades_success() {
        assertThat(this.analysisService.getIrrelevantTrades(LocalDate.of(2023, 2, 2), LocalDate.of(2023, 2, 18)))
                .isNotNull()
                .extracting("current")
                .isNotNull();
    }


    //  ----------------- getTradeDayBuckets -----------------

    @Test
    public void test_getTradeDayBuckets_success() {
        assertThat(this.analysisService.getTradeDayBuckets(LocalDate.of(2023, 2, 2), LocalDate.of(2023, 2, 18)))
                .isNotEmpty();
    }
}
