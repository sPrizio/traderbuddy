package com.traderbuddyv2.api.converters.trade.record;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.models.dto.trade.record.TradeRecordStatisticsDTO;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import com.traderbuddyv2.core.services.trade.TradeService;
import com.traderbuddyv2.core.services.trade.record.TradeRecordService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;

/**
 * Testing class for {@link TradeRecordStatisticsDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TradeRecordStatisticsDTOConverterTest extends AbstractGenericTest {

    @Mock
    private MathService mathService;

    @Mock
    private TradeService tradeService;

    @Mock
    private TradeRecordService tradeRecordService;

    @Mock
    private UniqueIdentifierService uniqueIdentifierService;

    @InjectMocks
    private TradeRecordStatisticsDTOConverter tradeRecordStatisticsDTOConverter;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.mathService.getDouble(184.05)).thenReturn(184.05);
        Mockito.when(this.mathService.subtract(anyDouble(), anyDouble())).thenReturn(0.0);
        Mockito.when(this.tradeRecordService.findRecentHistory(anyInt(), any())).thenReturn(List.of(generateTestTradeRecord()));
        Mockito.when(this.tradeService.findAllTradesWithinTimespan(any(), any(), anyBoolean())).thenReturn(List.of(generateTestBuyTrade(), generateTestSellTrade()));
    }


    //  ----------------- convert -----------------

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.tradeRecordStatisticsDTOConverter.convert(null))
                .isNotNull()
                .satisfies(TradeRecordStatisticsDTO::isEmpty);
    }

    @Test
    public void test_convert_success() {
        assertThat(this.tradeRecordStatisticsDTOConverter.convert(generateTestTradeRecordStatistics()))
                .isNotNull()
                .extracting("numberOfTrades", "winPercentage", "netProfit")
                .containsExactly(51, 59, 184.05);
    }


    //  ----------------- convertAll -----------------

    @Test
    public void test_convertAll_success() {
        assertThat(this.tradeRecordStatisticsDTOConverter.convertAll(List.of(generateTestTradeRecordStatistics())))
                .isNotEmpty()
                .first()
                .extracting("numberOfTrades", "winPercentage", "netProfit")
                .containsExactly(51, 59, 184.05);
    }
}
