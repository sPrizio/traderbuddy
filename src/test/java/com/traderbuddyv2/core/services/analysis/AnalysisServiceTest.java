package com.traderbuddyv2.core.services.analysis;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.analysis.AnalysisSort;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import com.traderbuddyv2.core.services.trade.TradeService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
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

    @Autowired
    private AnalysisService analysisService;

    @Before
    public void setUp() {
        Mockito.when(this.tradeService.findAllTradesWithinTimespan(any(), any(), anyBoolean())).thenReturn(List.of(generateTestBuyTrade(), generateTestSellTrade()));
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
}
