package com.stephenprizio.traderbuddy;

import com.stephenprizio.traderbuddy.enums.TradeType;
import com.stephenprizio.traderbuddy.enums.TradingPlatform;
import com.stephenprizio.traderbuddy.models.entities.Trade;

import java.time.LocalDateTime;

/**
 * Parent-level testing class to provide testing assistance
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
public abstract class AbstractTraderBuddyTest {

    public Trade generateTestBuyTrade() {

        Trade trade = new Trade();

        trade.setTradeId("testId1");
        trade.setTradingPlatform(TradingPlatform.CMC_MARKETS);
        trade.setResultOfTrade("Winner winner chicken dinner");
        trade.setTradeType(TradeType.BUY);
        trade.setClosePrice(13098.67);
        trade.setTradeCloseTime(LocalDateTime.of(2022, 8, 24, 11, 37, 24));
        trade.setTradeOpenTime(LocalDateTime.of(2022, 8, 24, 11, 32, 58));
        trade.setLotSize(0.75);
        trade.setNetProfit(14.85);
        trade.setOpenPrice(13083.41);
        trade.setReasonForEntrance("I have my reasons");

        return trade;
    }

    public Trade generateTestSellTrade() {

        Trade trade = new Trade();

        trade.setTradeId("testId2");
        trade.setTradingPlatform(TradingPlatform.CMC_MARKETS);
        trade.setResultOfTrade("Loser like a real loser");
        trade.setTradeType(TradeType.SELL);
        trade.setClosePrice(13156.12);
        trade.setTradeCloseTime(LocalDateTime.of(2022, 8, 24, 10, 24, 36));
        trade.setTradeOpenTime(LocalDateTime.of(2022, 8, 24, 10, 25, 12));
        trade.setLotSize(0.75);
        trade.setNetProfit(-4.50);
        trade.setOpenPrice(13160.09);
        trade.setReasonForEntrance("I continue to have my reasons");

        return trade;
    }
}
