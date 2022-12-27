package com.traderbuddyv2.api.converters.trade;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.models.dto.trade.TradeDTO;
import com.traderbuddyv2.core.enums.trades.TradeType;
import com.traderbuddyv2.core.enums.trades.TradingPlatform;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;

/**
 * Testing class for {@link TradeDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TradeDTOConverterTest extends AbstractGenericTest {

    @Autowired
    private TradeDTOConverter tradeDTOConverter;

    @MockBean
    private MathService mathService;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.mathService.subtract(anyDouble(), anyDouble())).thenReturn(0.0);
    }


    //  ----------------- convert -----------------

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.tradeDTOConverter.convert(null))
                .isNotNull()
                .satisfies(TradeDTO::isEmpty);

    }

    @Test
    public void test_convert_success() {
        assertThat(this.tradeDTOConverter.convert(generateTestBuyTrade()))
                .isNotNull()
                .extracting("tradeId", "tradingPlatform", "tradeType", "tradeOpenTime", "tradeCloseTime", "lotSize", "openPrice", "closePrice", "netProfit", "reasonForEntrance", "resultOfTrade")
                .containsExactly(
                        "testId1",
                        TradingPlatform.CMC_MARKETS,
                        TradeType.BUY,
                        LocalDateTime.of(2022, 8, 24, 11, 32, 58),
                        LocalDateTime.of(2022, 8, 24, 11, 37, 24),
                        0.75,
                        13083.41,
                        13098.67,
                        14.85,
                        "I have my reasons",
                        "Winner winner chicken dinner"
                );

    }


    //  ----------------- convertAll -----------------

    @Test
    public void test_convertAll_success() {
        assertThat(this.tradeDTOConverter.convertAll(List.of(generateTestBuyTrade())))
                .isNotEmpty()
                .first()
                .extracting("tradeId", "tradingPlatform", "tradeType", "tradeOpenTime", "tradeCloseTime", "lotSize", "openPrice", "closePrice", "netProfit", "reasonForEntrance", "resultOfTrade")
                .containsExactly(
                        "testId1",
                        TradingPlatform.CMC_MARKETS,
                        TradeType.BUY,
                        LocalDateTime.of(2022, 8, 24, 11, 32, 58),
                        LocalDateTime.of(2022, 8, 24, 11, 37, 24),
                        0.75,
                        13083.41,
                        13098.67,
                        14.85,
                        "I have my reasons",
                        "Winner winner chicken dinner"
                );
    }
}
