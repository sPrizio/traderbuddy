package com.traderbuddyv2.core.services.trade.record;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.account.AccountBalanceModification;
import com.traderbuddyv2.core.models.entities.plan.TradingPlan;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecordStatistics;
import com.traderbuddyv2.core.models.records.trade.MonthRecord;
import com.traderbuddyv2.core.repositories.account.AccountRepository;
import com.traderbuddyv2.core.repositories.trade.record.TradeRecordRepository;
import com.traderbuddyv2.core.repositories.trade.record.TradeRecordStatisticsRepository;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.plan.TradingPlanService;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import com.traderbuddyv2.core.services.trade.TradeService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static com.traderbuddyv2.core.validation.GenericValidator.*;

/**
 * Service-layer for {@link TradeRecord}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("tradeRecordService")
public class TradeRecordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeRecordService.class);

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "mathService")
    private MathService mathService;

    @Resource(name = "tradeRecordRepository")
    private TradeRecordRepository tradeRecordRepository;

    @Resource(name = "tradeRecordStatisticsRepository")
    private TradeRecordStatisticsRepository tradeRecordStatisticsRepository;

    @Resource(name = "tradeRecordStatisticsService")
    private TradeRecordStatisticsService tradeRecordStatisticsService;

    @Resource(name = "tradeService")
    private TradeService tradeService;

    @Resource(name = "traderBuddyUserDetailsService")
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @Resource(name = "tradingPlanService")
    private TradingPlanService tradingPlanService;


    //  METHODS

    /**
     * Finds the {@link TradeRecord} for the given start date, end date & interval
     *
     * @param startDate         {@link LocalDate}
     * @param endDate           {@link LocalDate}
     * @param aggregateInterval {@link AggregateInterval}
     * @return {@link Optional} {@link TradeRecord}
     */
    public Optional<TradeRecord> findTradeRecordForStartDateAndEndDateAndInterval(final LocalDate startDate, final LocalDate endDate, final AggregateInterval aggregateInterval) {

        validateParameterIsNotNull(startDate, CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(endDate, CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(aggregateInterval, CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(startDate.atStartOfDay(), endDate.atStartOfDay(), CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);

        return Optional.ofNullable(this.tradeRecordRepository.findTradeRecordByStartDateAndEndDateAndAggregateIntervalAndAccount(startDate, endDate, aggregateInterval, this.traderBuddyUserDetailsService.getCurrentUser().getAccount()));
    }

    /**
     * Obtains a {@link List} of {@link TradeRecord}s ordered by their end dates in descending order for the given {@link AggregateInterval}
     *
     * @param count             query limit
     * @param aggregateInterval {@link AggregateInterval}
     * @return {@link List} of {@link TradeRecord}
     */
    public List<TradeRecord> findRecentHistory(final int count, final AggregateInterval aggregateInterval) {
        validateParameterIsNotNull(aggregateInterval, CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);
        return this.tradeRecordRepository.findRecentHistory(count == CoreConstants.MAX_RESULT_SIZE ? 1000 : count, aggregateInterval.ordinal(), this.traderBuddyUserDetailsService.getCurrentUser().getAccount().getId());
    }

    /**
     * Obtains a {@link List} of {@link TradeRecord}s ordered by their start dates in ascending order that are within the given time span
     *
     * @param startDate         {@link LocalDate}
     * @param endDate           {@link LocalDate}
     * @param aggregateInterval {@link AggregateInterval}
     * @return {@link List} of {@link TradeRecord}
     */
    public List<TradeRecord> findHistory(final LocalDate startDate, final LocalDate endDate, final AggregateInterval aggregateInterval) {

        validateParameterIsNotNull(startDate, CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(endDate, CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(aggregateInterval, CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(startDate.atStartOfDay(), endDate.atStartOfDay(), CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);

        return this.tradeRecordRepository.findHistory(startDate, endDate, aggregateInterval, this.traderBuddyUserDetailsService.getCurrentUser().getAccount());
    }

    /**
     * Obtains a {@link List} of {@link MonthRecord}s for the given calendar year
     *
     * @param year calendar year
     * @return {@link List} of {@link MonthRecord}
     */
    public List<MonthRecord> findActiveMonths(final int year) {

        validateAcceptableYear(year, StringUtils.EMPTY);

        final List<MonthRecord> monthRecords = new ArrayList<>();
        final Map<Month, TradeRecord> tradeRecordMap = new EnumMap<>(Month.class);
        findHistory(LocalDate.of(year, 1, 1), LocalDate.of(year + 1, 1, 2), AggregateInterval.MONTHLY).forEach(rec -> tradeRecordMap.put(rec.getStartDate().getMonth(), rec));

        for (int i = 1; i < 13; i++) {
            Month month = getMonth(i);
            TradeRecord tradeRecord = tradeRecordMap.getOrDefault(month, null);
            monthRecords.add(generateMonthRecord(month, tradeRecord));
        }

        return monthRecords;
    }

    /**
     * Obtains a {@link TradeRecord} that contains the given {@link Trade} for the given {@link AggregateInterval}
     *
     * @param trade             {@link Trade}
     * @param aggregateInterval {@link AggregateInterval}
     * @return {@link Optional} {@link TradeRecord}
     */
    public Optional<TradeRecord> findTradeRecordForTrade(final Trade trade, final AggregateInterval aggregateInterval) {

        validateParameterIsNotNull(trade, "trade cannot be null");
        validateParameterIsNotNull(aggregateInterval, CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);

        return findTradeRecordForStartDateAndEndDateAndInterval(computeStartDate(trade.getTradeCloseTime().toLocalDate(), aggregateInterval), computeEndDate(trade.getTradeCloseTime().toLocalDate(), aggregateInterval), aggregateInterval);
    }

    /**
     * Obtains the previous {@link TradeRecord} for the given one
     * Note: The previous one refers to the next closest record in the past
     *
     * @param tradeRecord {@link TradeRecord}
     * @return {@link Optional} {@link TradeRecord}
     */
    public Optional<TradeRecord> findPreviousTradeRecord(final TradeRecord tradeRecord) {

        validateParameterIsNotNull(tradeRecord, "trade record cannot be null");

        List<TradeRecord> recent = findHistory(tradeRecord.getStartDate().minusYears(1), tradeRecord.getEndDate(), tradeRecord.getAggregateInterval());
        if (CollectionUtils.isNotEmpty(recent)) {
            return Optional.of(recent.get(recent.size() - 1));
        }

        return Optional.empty();
    }

    /**
     * A disregarded trade should have its information removed from a {@link TradeRecord}
     *
     * @param tradeId trade id
     */
    public void processDisregardedTrade(final String tradeId) {

        validateParameterIsNotNull(tradeId, "trade id cannot be null");

        final Account account = this.traderBuddyUserDetailsService.getCurrentUser().getAccount();
        final Optional<Trade> trade = this.tradeService.findTradeByTradeId(tradeId);

        trade.ifPresent(tr -> {
            findTradeRecordForTrade(tr, AggregateInterval.YEARLY).ifPresent(rec -> updateTradeRecord(tr, rec));
            findTradeRecordForTrade(tr, AggregateInterval.MONTHLY).ifPresent(rec -> updateTradeRecord(tr, rec));
            findTradeRecordForTrade(tr, AggregateInterval.WEEKLY).ifPresent(rec -> updateTradeRecord(tr, rec));

            findTradeRecordForTrade(tr, AggregateInterval.DAILY).ifPresent(rec -> {
                if (rec.getStatistics() != null) {
                    account.setBalance(this.mathService.subtract(account.getBalance(), rec.getStatistics().getNetProfit()));
                    this.accountRepository.save(account);

                    updateTradeRecord(tr, rec);
                }
            });
        });

        LOGGER.info("Trade processing completed without issue");
    }

    /**
     * Processes all unprocessed {@link Trade}s
     */
    public void processTrades() {

        final Account account = this.traderBuddyUserDetailsService.getCurrentUser().getAccount();
        final List<Trade> trades = this.tradeService.findTradesByProcessed(false);

        generateTradeRecord(account, trades, AggregateInterval.YEARLY);
        generateTradeRecord(account, trades, AggregateInterval.MONTHLY);
        generateTradeRecord(account, trades, AggregateInterval.WEEKLY);
        generateTradeRecord(account, trades, AggregateInterval.DAILY);

        trades.forEach(trade -> this.tradeService.processTrade(trade));
        LOGGER.info("Trade processing completed without issue");
    }


    //  HELPER

    /**
     * Computes the start date based on the given interval
     *
     * @param localDate         {@link LocalDate}
     * @param aggregateInterval {@link AggregateInterval}
     * @return {@link LocalDate}
     */
    private LocalDate computeStartDate(final LocalDate localDate, final AggregateInterval aggregateInterval) {
        return
                switch (aggregateInterval) {
                    case WEEKLY -> {
                        LocalDate compare = localDate;
                        while (compare.getDayOfWeek() != DayOfWeek.MONDAY) {
                            compare = compare.minusDays(1);
                        }

                        yield compare;
                    }
                    case MONTHLY -> localDate.with(TemporalAdjusters.firstDayOfMonth());
                    case YEARLY -> localDate.with(TemporalAdjusters.firstDayOfYear());
                    default -> localDate;
                };
    }

    /**
     * Computes the end date based on the given interval
     *
     * @param localDate         {@link LocalDate}
     * @param aggregateInterval {@link AggregateInterval}
     * @return {@link LocalDate}
     */
    private LocalDate computeEndDate(final LocalDate localDate, final AggregateInterval aggregateInterval) {
        return
                switch (aggregateInterval) {
                    case WEEKLY -> {
                        LocalDate now = localDate.plusWeeks(1);
                        while (now.getDayOfWeek() != DayOfWeek.MONDAY) {
                            now = now.minusDays(1);
                        }

                        yield now;
                    }
                    case MONTHLY -> localDate.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth());
                    case YEARLY -> localDate.plusYears(1).with(TemporalAdjusters.firstDayOfYear());
                    default -> localDate.plusDays(1);
                };
    }

    /**
     * Filters a list of trades to only that ones that fall between the given start and end dates
     *
     * @param start  {@link LocalDate} start
     * @param end    {@link LocalDate} end
     * @param trades {@link List} of {@link Trade}s
     * @return subset {@link List} of {@link Trade}s
     */
    private List<Trade> filterTrades(final LocalDate start, final LocalDate end, final List<Trade> trades) {

        if (CollectionUtils.isNotEmpty(trades)) {
            return trades
                    .stream()
                    .filter(trade -> (trade.getTradeOpenTime().toLocalDate().isAfter(start) || trade.getTradeOpenTime().toLocalDate().isEqual(start)))
                    .filter(trade -> (trade.getTradeOpenTime().toLocalDate().isBefore(end)))
                    .toList();
        }

        return Collections.emptyList();
    }

    /**
     * Generates or updates a {@link TradeRecord}
     *
     * @param account           {@link Account}
     * @param trades            {@link List} of {@link Trade}
     * @param aggregateInterval {@link AggregateInterval}
     */
    private void generateTradeRecord(final Account account, final List<Trade> trades, final AggregateInterval aggregateInterval) {

        validateParameterIsNotNull(account, "account cannot be null");
        validateParameterIsNotNull(trades, "trades cannot be null");
        validateParameterIsNotNull(aggregateInterval, CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);

        Set<Pair<LocalDate, LocalDate>> buckets = new TreeSet<>();
        trades.forEach(trade -> buckets.add(Pair.of(computeStartDate(trade.getTradeCloseTime().toLocalDate(), aggregateInterval), computeEndDate(trade.getTradeCloseTime().toLocalDate(), aggregateInterval))));

        buckets.forEach(bucket -> {
            TradeRecord tradeRecord = findTradeRecordForStartDateAndEndDateAndInterval(bucket.getLeft(), bucket.getRight(), aggregateInterval).orElse(new TradeRecord());
            List<Trade> filtered = filterTrades(bucket.getLeft(), bucket.getRight(), trades);
            Optional<TradingPlan> tradingPlan = this.tradingPlanService.findCurrentlyActiveTradingPlan();

            double profit = filtered.stream().mapToDouble(Trade::getNetProfit).sum();
            double balance = computeBalance(account, bucket.getLeft(), bucket.getRight());

            tradeRecord.setStartDate(bucket.getLeft());
            tradeRecord.setEndDate(bucket.getRight());
            tradeRecord.setAggregateInterval(aggregateInterval);
            tradeRecord.setBalance(balance);
            tradeRecord.setAccount(account);

            if (tradingPlan.isPresent()) {
                if (tradingPlan.get().isAbsolute()) {
                    tradeRecord.setTarget(tradingPlan.get().getProfitTarget());
                } else {
                    tradeRecord.setTarget(this.mathService.computeIncrement(balance, tradingPlan.get().getProfitTarget(), false));
                }
            }

            tradeRecord = this.tradeRecordRepository.save(tradeRecord);
            tradeRecord.setStatistics(this.tradeRecordStatisticsService.generateStatistics(tradeRecord, filtered));

            this.tradeRecordRepository.save(tradeRecord);
            if (aggregateInterval.equals(AggregateInterval.DAILY)) {
                updateCurrentAccountBalance(
                        this.mathService.add(profit,
                                account.getBalanceModifications()
                                        .stream()
                                        .filter(mod -> bucket.getLeft().atStartOfDay().isBefore(mod.getDateTime()) || bucket.getLeft().atStartOfDay().isEqual(mod.getDateTime()))
                                        .filter(mod -> bucket.getRight().atStartOfDay().isAfter(mod.getDateTime()))
                                        .mapToDouble(AccountBalanceModification::getAmount)
                                        .sum())
                );
            }
        });
    }

    /**
     * Updates the given {@link TradeRecord} with all trades excluding the given trade
     *
     * @param trade       {@link Trade}
     * @param tradeRecord {@link TradeRecord}
     */
    private void updateTradeRecord(final Trade trade, final TradeRecord tradeRecord) {

        if (tradeRecord == null) {
            return;
        }

        final List<Trade> trades =
                this.tradeService.findAllTradesWithinTimespan(tradeRecord.getStartDate().atStartOfDay(), tradeRecord.getEndDate().atStartOfDay(), false)
                        .stream()
                        .filter(tr -> !tr.getTradeId().equals(trade.getTradeId()))
                        .toList();

        //  update balance
        tradeRecord.setBalance(this.mathService.subtract(tradeRecord.getBalance(), trade.getNetProfit()));

        //  clear the statistics
        TradeRecordStatistics tradeRecordStatistics = tradeRecord.getStatistics();
        tradeRecord.setStatistics(null);
        this.tradeRecordRepository.save(tradeRecord);

        if (tradeRecordStatistics != null) {
            this.tradeRecordStatisticsRepository.delete(tradeRecordStatistics);
        }

        if (trades.isEmpty()) {
            this.tradeRecordRepository.delete(tradeRecord);
            return;
        }

        generateTradeRecord(this.traderBuddyUserDetailsService.getCurrentUser().getAccount(), trades, tradeRecord.getAggregateInterval());
    }


    //  HELPERS

    /**
     * Updates the currently logged in {@link Account}'s balance
     *
     * @param amount amount to add/subtract
     */
    private void updateCurrentAccountBalance(final double amount) {

        final Account account = this.traderBuddyUserDetailsService.getCurrentUser().getAccount();
        if (account != null) {
            account.setBalance(
                    this.mathService.add(
                            amount, account.getBalance()
                    )
            );

            this.accountRepository.save(account);
        }
    }

    /**
     * Returns a {@link Month} for the given index
     *
     * @param i month index (1-indexed, not zero)
     * @return {@link Month}
     */
    private Month getMonth(final int i) {
        return
                switch (i) {
                    case 1 -> Month.JANUARY;
                    case 2 -> Month.FEBRUARY;
                    case 3 -> Month.MARCH;
                    case 4 -> Month.APRIL;
                    case 5 -> Month.MAY;
                    case 6 -> Month.JUNE;
                    case 7 -> Month.JULY;
                    case 8 -> Month.AUGUST;
                    case 9 -> Month.SEPTEMBER;
                    case 10 -> Month.OCTOBER;
                    case 11 -> Month.NOVEMBER;
                    default -> Month.DECEMBER;
                };
    }

    /**
     * Generates a {@link MonthRecord} from a {@link TradeRecord} & {@link Month}
     *
     * @param month       {@link Month}
     * @param tradeRecord {@link TradeRecord}
     * @return {@link MonthRecord}
     */
    private MonthRecord generateMonthRecord(final Month month, final TradeRecord tradeRecord) {

        if (tradeRecord == null) {
            return new MonthRecord(month, false, 0, 0.0);
        }

        return new MonthRecord(
                tradeRecord.getStartDate().getMonth(),
                tradeRecord.getStatistics().getNumberOfTrades() > 0,
                tradeRecord.getStatistics().getNumberOfTrades(),
                this.mathService.getDouble(tradeRecord.getStatistics().getNetProfit())
        );
    }

    /**
     * Computes the balance for the {@link TradeRecord}. The balance will not include the profit of that record
     *
     * @param account {@link Account}
     * @param start   start of period
     * @param end     end of period
     * @return balance for start of period
     */
    private double computeBalance(final Account account, final LocalDate start, final LocalDate end) {
        return
                this.mathService.add(
                        account.getBalance(),
                        account.getBalanceModifications()
                                .stream()
                                .filter(mod -> start.atStartOfDay().isBefore(mod.getDateTime()) || start.atStartOfDay().isEqual(mod.getDateTime()))
                                .filter(mod -> end.atStartOfDay().isAfter(mod.getDateTime()))
                                .mapToDouble(AccountBalanceModification::getAmount)
                                .sum()
                );
    }
}

