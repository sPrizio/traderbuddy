package com.stephenprizio.traderbuddy;

import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.enums.plans.TradingPlanStatus;
import com.stephenprizio.traderbuddy.enums.trades.TradeType;
import com.stephenprizio.traderbuddy.enums.trades.TradingPlatform;
import com.stephenprizio.traderbuddy.models.dto.plans.DepositPlanDTO;
import com.stephenprizio.traderbuddy.models.dto.plans.WithdrawalPlanDTO;
import com.stephenprizio.traderbuddy.models.entities.plans.DepositPlan;
import com.stephenprizio.traderbuddy.models.entities.plans.TradingPlan;
import com.stephenprizio.traderbuddy.models.entities.plans.WithdrawalPlan;
import com.stephenprizio.traderbuddy.models.entities.trades.Trade;
import com.stephenprizio.traderbuddy.models.records.reporting.TradingRecord;
import com.stephenprizio.traderbuddy.models.records.reporting.TradingSummary;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Parent-level testing class to provide testing assistance
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public abstract class AbstractGenericTest {

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
        trade.setRelevant(true);

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
        trade.setRelevant(true);

        return trade;
    }

    public List<Trade> generateTrades(Integer count) {

        Random random = new Random();
        List<Trade> trades = new ArrayList<>();
        LocalDateTime test = LocalDateTime.of(2022, 8, 24, 10, 0, 0);

        while (trades.size() < count) {
            Trade trade = new Trade();

            trade.setTradeId("testId" + test.getDayOfYear());
            trade.setTradingPlatform(TradingPlatform.CMC_MARKETS);
            trade.setResultOfTrade(StringUtils.EMPTY);
            trade.setTradeType(TradeType.BUY);
            trade.setClosePrice(0.0);
            trade.setTradeCloseTime(test.plusMinutes(7));
            trade.setTradeOpenTime(test.plusMinutes(2));
            trade.setLotSize(0.75);
            trade.setNetProfit(BigDecimal.valueOf(random.nextDouble(15.0 + 10.0) - 10.0).setScale(2, RoundingMode.HALF_EVEN).doubleValue());
            trade.setOpenPrice(0.0);
            trade.setReasonForEntrance(StringUtils.EMPTY);
            trade.setRelevant(true);

            trades.add(trade);

            test = test.plusDays(1);
        }

        return trades;
    }

    public TradingSummary generateTradingSummary() {
        return new TradingSummary(List.of(new TradingRecord(LocalDateTime.MAX, 47.52, 15, 67, 58.63, 1.25, 11.11, true)), null);
    }

    public DepositPlan generateDepositPlan() {
        DepositPlan depositPlan = new DepositPlan();

        depositPlan.setAmount(350.0);
        depositPlan.setFrequency(CompoundFrequency.MONTHLY);

        return depositPlan;
    }

    public WithdrawalPlan generateWithdrawalPlan() {
        WithdrawalPlan withdrawalPlan = new WithdrawalPlan();

        withdrawalPlan.setAmount(120.0);
        withdrawalPlan.setFrequency(CompoundFrequency.MONTHLY);

        return withdrawalPlan;
    }

    public DepositPlanDTO generateDepositPlanDTO() {
        DepositPlanDTO depositPlan = new DepositPlanDTO();

        depositPlan.setAmount(350.0);
        depositPlan.setFrequency(CompoundFrequency.MONTHLY);

        return depositPlan;
    }

    public WithdrawalPlanDTO generateWithdrawalPlanDTO() {
        WithdrawalPlanDTO withdrawalPlan = new WithdrawalPlanDTO();

        withdrawalPlan.setAmount(120.0);
        withdrawalPlan.setFrequency(CompoundFrequency.MONTHLY);

        return withdrawalPlan;
    }

    public TradingPlan generateTestTradingPlan() {
        TradingPlan tradingPlan = new TradingPlan();

        tradingPlan.setActive(true);
        tradingPlan.setName("Test Trading Plan Active");
        tradingPlan.setStartDate(LocalDate.of(2022, 1, 1));
        tradingPlan.setEndDate(LocalDate.of(2025, 1, 1));
        tradingPlan.setProfitTarget(528491.0);
        tradingPlan.setStatus(TradingPlanStatus.IN_PROGRESS);
        tradingPlan.setCompoundFrequency(CompoundFrequency.DAILY);
        tradingPlan.setStartingBalance(1000.0);
        tradingPlan.setDepositPlan(generateDepositPlan());
        tradingPlan.setWithdrawalPlan(generateWithdrawalPlan());

        return tradingPlan;
    }

    public TradingPlan generateInactiveTestTradingPlan() {
        TradingPlan tradingPlan = new TradingPlan();

        tradingPlan.setActive(false);
        tradingPlan.setName("Test Trading Plan Inactive");
        tradingPlan.setStartDate(LocalDate.of(2023, 1, 1));
        tradingPlan.setEndDate(LocalDate.of(2025, 1, 1));
        tradingPlan.setProfitTarget(12345.0);
        tradingPlan.setStatus(TradingPlanStatus.NOT_STARTED);
        tradingPlan.setDepositPlan(generateDepositPlan());
        tradingPlan.setWithdrawalPlan(generateWithdrawalPlan());

        return tradingPlan;
    }
}
