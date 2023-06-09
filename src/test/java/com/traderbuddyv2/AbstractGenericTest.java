package com.traderbuddyv2;

import com.traderbuddyv2.api.models.dto.account.AccountDTO;
import com.traderbuddyv2.api.models.dto.plans.DepositPlanDTO;
import com.traderbuddyv2.api.models.dto.plans.WithdrawalPlanDTO;
import com.traderbuddyv2.api.models.records.account.AccountOverview;
import com.traderbuddyv2.core.enums.account.*;
import com.traderbuddyv2.core.enums.account.Broker;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.enums.news.MarketNewsSeverity;
import com.traderbuddyv2.core.enums.plans.TradingPlanStatus;
import com.traderbuddyv2.core.enums.security.UserRole;
import com.traderbuddyv2.core.enums.system.Country;
import com.traderbuddyv2.core.enums.system.Language;
import com.traderbuddyv2.core.enums.system.PhoneType;
import com.traderbuddyv2.core.enums.trade.info.TradeType;
import com.traderbuddyv2.core.enums.trade.platform.TradePlatform;
import com.traderbuddyv2.core.enums.trade.tag.TradeEntryReason;
import com.traderbuddyv2.core.enums.trade.tag.TradeResult;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.account.AccountBalanceModification;
import com.traderbuddyv2.core.models.entities.levelling.rank.BaseRank;
import com.traderbuddyv2.core.models.entities.levelling.rank.Rank;
import com.traderbuddyv2.core.models.entities.levelling.skill.Skill;
import com.traderbuddyv2.core.models.entities.news.MarketNews;
import com.traderbuddyv2.core.models.entities.news.MarketNewsEntry;
import com.traderbuddyv2.core.models.entities.news.MarketNewsSlot;
import com.traderbuddyv2.core.models.entities.plan.DepositPlan;
import com.traderbuddyv2.core.models.entities.plan.TradingPlan;
import com.traderbuddyv2.core.models.entities.plan.WithdrawalPlan;
import com.traderbuddyv2.core.models.entities.retrospective.Retrospective;
import com.traderbuddyv2.core.models.entities.retrospective.RetrospectiveEntry;
import com.traderbuddyv2.core.models.entities.security.User;
import com.traderbuddyv2.core.models.entities.security.UserLocale;
import com.traderbuddyv2.core.models.entities.system.PhoneNumber;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecordStatistics;
import com.traderbuddyv2.core.models.nonentities.analysis.performance.AverageTradePerformance;
import com.traderbuddyv2.core.models.records.plan.ForecastEntry;
import com.traderbuddyv2.core.models.records.rank.CurrentRank;
import com.traderbuddyv2.integration.models.dto.eod.IntradayHistoricalDataDTO;
import com.traderbuddyv2.integration.models.dto.eod.IntradayHistoricalDataEntryDTO;
import com.traderbuddyv2.integration.models.response.eod.IntradayHistoricalDataEntryResponse;
import com.traderbuddyv2.integration.models.response.eod.IntradayHistoricalDataResponse;
import com.traderbuddyv2.integration.models.response.forexfactory.CalendarNewsEntryResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

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
        trade.setTradePlatform(TradePlatform.CMC_MARKETS);
        trade.setResultsOfTrade(List.of(TradeResult.EXCELLENT_CALL));
        trade.setTradeType(TradeType.BUY);
        trade.setClosePrice(13098.67);
        trade.setTradeCloseTime(LocalDateTime.of(2022, 8, 24, 11, 37, 24));
        trade.setTradeOpenTime(LocalDateTime.of(2022, 8, 24, 11, 32, 58));
        trade.setLotSize(0.75);
        trade.setNetProfit(14.85);
        trade.setOpenPrice(13083.41);
        trade.setReasonsForEntry(List.of(TradeEntryReason.DOUBLE_TOP));
        trade.setRelevant(true);
        trade.setProcessed(true);
        trade.setAccount(generateTestAccount());

        return trade;
    }

    public Trade generateTestSellTrade() {

        Trade trade = new Trade();

        trade.setTradeId("testId2");
        trade.setTradePlatform(TradePlatform.CMC_MARKETS);
        trade.setResultsOfTrade(List.of(TradeResult.FAILED_IDEA));
        trade.setTradeType(TradeType.SELL);
        trade.setClosePrice(13156.12);
        trade.setTradeCloseTime(LocalDateTime.of(2022, 8, 24, 10, 24, 36));
        trade.setTradeOpenTime(LocalDateTime.of(2022, 8, 24, 10, 25, 12));
        trade.setLotSize(0.75);
        trade.setNetProfit(-4.50);
        trade.setOpenPrice(13160.09);
        trade.setReasonsForEntry(List.of(TradeEntryReason.DOUBLE_BOTTOM));
        trade.setRelevant(true);
        trade.setProcessed(false);

        return trade;
    }

    public TradeRecordStatistics generateTestTradeRecordStatistics() {

        final TradeRecordStatistics statistics = new TradeRecordStatistics();

        statistics.setNumberOfTrades(51);
        statistics.setNetProfit(184.05);
        statistics.setWinPercentage(59);
        statistics.setPercentageProfit(1.78);
        statistics.setNumberOfWinningTrades(30);
        statistics.setNumberOfLosingTrades(21);
        statistics.setAverageWinAmount(13.56);
        statistics.setAverageWinSize(1.1);
        statistics.setAverageLossAmount(15.69);
        statistics.setAverageLossSize(2);
        statistics.setLargestWinAmount(75.21);
        statistics.setLargestWinSize(2.25);
        statistics.setLargestLossAmount(148.56);
        statistics.setLargestLossSize(2.1);
        statistics.setTradingRate(17);
        statistics.setPipsEarned(301);
        statistics.setPipsLost(115);

        TradeRecord tradeRecord = new TradeRecord();
        tradeRecord.setStartDate(LocalDate.of(2022, 8, 24));
        tradeRecord.setEndDate(LocalDate.of(2022, 8, 25));
        tradeRecord.setAggregateInterval(AggregateInterval.MONTHLY);
        statistics.setTradeRecord(tradeRecord);

        return statistics;
    }

    public TradeRecord generateTestTradeRecord() {

        final TradeRecord tradeRecord = new TradeRecord();

        tradeRecord.setStartDate(LocalDate.of(2022, 8, 24));
        tradeRecord.setEndDate(LocalDate.of(2022, 8, 25));
        tradeRecord.setAggregateInterval(AggregateInterval.DAILY);
        tradeRecord.setBalance(1000.0);
        tradeRecord.setStatistics(generateTestTradeRecordStatistics());
        tradeRecord.setAccount(generateTestAccount());

        return tradeRecord;
    }

    public List<RetrospectiveEntry> generateEntries() {

        RetrospectiveEntry entry1 = new RetrospectiveEntry();
        RetrospectiveEntry entry2 = new RetrospectiveEntry();

        entry1.setKeyPoint(true);
        entry1.setEntryText("Test 1");
        entry1.setLineNumber(1);

        entry2.setKeyPoint(true);
        entry2.setEntryText("Test 2");
        entry2.setLineNumber(1);

        return List.of(entry1, entry2);
    }

    public List<Retrospective> generateRetrospectives() {

        Retrospective retrospective1 = new Retrospective();
        Retrospective retrospective2 = new Retrospective();

        retrospective1.setStartDate(LocalDate.of(2022, 9, 5));
        retrospective1.setEndDate(LocalDate.of(2022, 9, 11));
        retrospective1.setIntervalFrequency(AggregateInterval.MONTHLY);
        retrospective1.setPoints(List.of(new RetrospectiveEntry()));

        retrospective2.setStartDate(LocalDate.of(2022, 9, 12));
        retrospective2.setEndDate(LocalDate.of(2022, 9, 18));
        retrospective2.setIntervalFrequency(AggregateInterval.WEEKLY);

        return List.of(retrospective1, retrospective2);
    }

    public IntradayHistoricalDataResponse generateIntradayResponse() {
        return new IntradayHistoricalDataResponse(LocalDate.MAX, "Test", "Test", List.of(new IntradayHistoricalDataEntryResponse(1564752900, 0, "2019-08-02 13:35:00", 205.14, 205.37, 204.75, 204.7683, 231517)));
    }

    public IntradayHistoricalDataDTO generateIntradayDto() {

        IntradayHistoricalDataEntryDTO entryDTO = new IntradayHistoricalDataEntryDTO();

        entryDTO.setClose(204.7683);
        entryDTO.setHigh(205.37);
        entryDTO.setOpen(205.14);
        entryDTO.setLow(204.75);
        entryDTO.setDatetime(LocalDateTime.of(2019, 8, 2, 13, 35, 0));
        entryDTO.setVolume(231517);


        IntradayHistoricalDataDTO dto = new IntradayHistoricalDataDTO();
        dto.setEntries(List.of(entryDTO));
        return dto;
    }

    public Account generateTestAccount() {

        Account account = new Account();

        account.setId(-1L);
        account.setDefaultAccount(true);
        account.setAccountOpenTime(LocalDateTime.of(2022, 10, 25, 22, 48, 0));
        account.setBalance(1000.0);
        account.setActive(true);
        account.setRetrospectives(new ArrayList<>());
        account.setBalanceModifications(List.of());
        account.setAccountType(AccountType.CFD);
        account.setAccountNumber(1234);
        account.setDailyStopLimit(55.0);
        account.setDailyStopLimitType(StopLimitType.PIPS);
        account.setName("Test Account");
        account.setCurrency(Currency.CANADIAN_DOLLAR);
        account.setBroker(Broker.CMC_MARKETS);
        account.setSkill(generateTestSkill());
        account.setRank(generateTestRank());
        account.setTradePlatform(TradePlatform.CMC_MARKETS);

        return account;
    }

    public AccountBalanceModification generateTestAccountBalanceModification() {

        AccountBalanceModification modification = new AccountBalanceModification();

        modification.setModificationType(AccountBalanceModificationType.ONE_TIME_DEPOSIT);
        modification.setAmount(350.0);
        modification.setDateTime(LocalDateTime.of(2022, 9, 12, 1, 1, 1));
        modification.setProcessed(true);

        return modification;
    }

    public AccountDTO generateTestAccountDTO() {

        AccountDTO accountDTO = new AccountDTO();

        accountDTO.setAccountOpenTime(LocalDateTime.of(2022, 10, 25, 22, 48, 0));
        accountDTO.setBalance(1000.0);
        accountDTO.setActive(true);

        return accountDTO;
    }

    public User generateTestUser() {
        User user = new User();

        user.setAccounts(List.of(generateTestAccount()));
        user.setEmail("test@email.com");
        user.setUsername("s.prizio");
        user.setPassword("1234");
        user.setFirstName("Stephen");
        user.setLastName("Test");
        user.setUserLocale(generateTestUserLocale());
        user.setPhone(generateTestPhoneNumber());
        user.setRoles(List.of(UserRole.ADMINISTRATOR, UserRole.TRADER));

        return user;
    }

    public DepositPlan generateDepositPlan() {
        DepositPlan depositPlan = new DepositPlan();

        depositPlan.setAmount(350.0);
        depositPlan.setAbsolute(true);
        depositPlan.setAggregateInterval(AggregateInterval.MONTHLY);

        return depositPlan;
    }

    public WithdrawalPlan generateWithdrawalPlan() {
        WithdrawalPlan withdrawalPlan = new WithdrawalPlan();

        withdrawalPlan.setAmount(120.0);
        withdrawalPlan.setAbsolute(true);
        withdrawalPlan.setAggregateInterval(AggregateInterval.MONTHLY);

        return withdrawalPlan;
    }

    public DepositPlanDTO generateDepositPlanDTO() {
        DepositPlanDTO depositPlan = new DepositPlanDTO();

        depositPlan.setAmount(350.0);
        depositPlan.setAbsolute(true);
        depositPlan.setAggregateInterval(AggregateInterval.MONTHLY);

        return depositPlan;
    }

    public WithdrawalPlanDTO generateWithdrawalPlanDTO() {
        WithdrawalPlanDTO withdrawalPlan = new WithdrawalPlanDTO();

        withdrawalPlan.setAmount(120.0);
        withdrawalPlan.setAbsolute(true);
        withdrawalPlan.setAggregateInterval(AggregateInterval.MONTHLY);

        return withdrawalPlan;
    }

    public TradingPlan generateTestTradingPlan() {
        TradingPlan tradingPlan = new TradingPlan();

        tradingPlan.setActive(true);
        tradingPlan.setName("Test Trading Plan Active");
        tradingPlan.setStartDate(LocalDate.of(2022, 1, 1));
        tradingPlan.setEndDate(LocalDate.of(2025, 1, 1));
        tradingPlan.setProfitTarget(1.25);
        tradingPlan.setAbsolute(false);
        tradingPlan.setStatus(TradingPlanStatus.IN_PROGRESS);
        tradingPlan.setAggregateInterval(AggregateInterval.DAILY);
        tradingPlan.setStartingBalance(1000.0);
        tradingPlan.setDepositPlan(generateDepositPlan());
        tradingPlan.setWithdrawalPlan(generateWithdrawalPlan());
        tradingPlan.setAccount(generateTestAccount());

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
        tradingPlan.setAbsolute(true);
        tradingPlan.setAggregateInterval(AggregateInterval.YEARLY);

        return tradingPlan;
    }

    public AccountOverview generateAccountOverview() {
        return new AccountOverview(
                LocalDateTime.of(2022, 8, 24, 0, 0, 0),
                1025.0,
                25.0,
                25.0,
                41.38,
                null,
                generateTestCurrentRank()
        );
    }

    public CurrentRank generateTestCurrentRank() {
        return new CurrentRank(1L, "Test", 1000, 1100, 1200, "test.png", "class", null, null);
    }

    public List<ForecastEntry> generateForecast() {
        return List.of(
                new ForecastEntry(LocalDate.MIN, LocalDate.MAX, 1.0, 1.0, 1.0, 1.0, 1.0, 1.0)
        );
    }

    public AverageTradePerformance generateAverageTradePerformance() {

        final AverageTradePerformance averageTradePerformance = new AverageTradePerformance();

        averageTradePerformance.setTotalTrades(2);
        averageTradePerformance.setProfitability(1.89);

        return averageTradePerformance;
    }

    public Skill generateTestSkill() {

        final Skill skill = new Skill();

        skill.setStepIncrement(100);
        skill.setPoints(38);
        skill.setLevel(2);

        return skill;
    }

    public BaseRank generateTestBaseRank() {

        final BaseRank baseRank = new BaseRank();

        baseRank.setMultiplier(1);
        baseRank.setPriority(1);
        baseRank.setName("Test Base Rank");
        baseRank.setRanks(List.of());

        return baseRank;
    }

    public Rank generateTestRank() {

        final Rank rank = new Rank();

        rank.setBaseRank(generateTestBaseRank());
        rank.setLevel(1);

        return rank;
    }

    public MarketNewsEntry generateTestMarketNewsEntry() {

        final MarketNewsEntry marketNewsEntry = new MarketNewsEntry();

        marketNewsEntry.setContent("Test News Entry");
        marketNewsEntry.setSeverity(MarketNewsSeverity.DANGEROUS);
        marketNewsEntry.setCountry(Country.CANADA);

        return marketNewsEntry;
    }

    public MarketNewsSlot generateTestMarketNewsSlot() {

        final MarketNewsSlot marketNewsSlot = new MarketNewsSlot();

        marketNewsSlot.setTime(LocalTime.of(13, 10));
        marketNewsSlot.setEntries(new ArrayList<>(List.of(generateTestMarketNewsEntry())));

        return marketNewsSlot;
    }

    public MarketNews generateMarketNews() {

        final MarketNews marketNews = new MarketNews();

        marketNews.setDate(LocalDate.of(2023, 1, 19));
        marketNews.setSlots(new ArrayList<>(List.of(generateTestMarketNewsSlot())));

        return marketNews;
    }

    public UserLocale generateTestUserLocale() {

        final UserLocale userLocale = new UserLocale();

        userLocale.setCountry(Country.CANADA);
        userLocale.setLanguages(List.of(Language.ENGLISH, Language.FRENCH));
        userLocale.setTownCity("Montreal");
        userLocale.setTimeZoneOffset("America/Toronto (GMT -05:00)");
        userLocale.setCurrencies(List.of(Currency.CANADIAN_DOLLAR));

        return userLocale;
    }

    public PhoneNumber generateTestPhoneNumber() {

        final PhoneNumber phoneNumber = new PhoneNumber();

        phoneNumber.setPhoneType(PhoneType.MOBILE);
        phoneNumber.setTelephoneNumber(1112223333);
        phoneNumber.setCountryCode((short) 1);

        return phoneNumber;
    }

    public CalendarNewsEntryResponse generateCalendarNewsEntryResponse() {
        return new CalendarNewsEntryResponse("Currenct Account", "CAD", "2023-05-30T08:30:00-04:00", "Low", "-9.9B", "-10.6B");
    }
}
