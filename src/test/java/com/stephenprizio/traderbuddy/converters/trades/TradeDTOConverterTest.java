package com.stephenprizio.traderbuddy.converters.trades;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.trades.TradeType;
import com.stephenprizio.traderbuddy.enums.trades.TradingPlatform;
import com.stephenprizio.traderbuddy.models.dto.trades.TradeDTO;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link TradeDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradeDTOConverterTest extends AbstractGenericTest {

    private final TradeDTOConverter tradeDTOConverter = new TradeDTOConverter();

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
