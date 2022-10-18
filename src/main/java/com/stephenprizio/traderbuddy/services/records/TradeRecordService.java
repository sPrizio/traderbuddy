package com.stephenprizio.traderbuddy.services.records;

import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import com.stephenprizio.traderbuddy.models.entities.records.TradeRecord;
import com.stephenprizio.traderbuddy.models.entities.trades.Trade;
import com.stephenprizio.traderbuddy.repositories.records.TradeRecordRepository;
import com.stephenprizio.traderbuddy.services.math.NumberService;
import com.stephenprizio.traderbuddy.services.trades.TradeService;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link TradeRecord}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("tradeRecordService")
public class TradeRecordService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeRecordService.class);

    @Resource(name = "numberService")
    private NumberService numberService;

    @Resource(name = "tradeRecordRepository")
    private TradeRecordRepository tradeRecordRepository;

    @Resource(name = "tradeService")
    private TradeService tradeService;


    //  METHODS

    /**
     * Finds the {@link TradeRecord} for the given start date, end date & interval
     *
     * @param startDate         {@link LocalDate}
     * @param endDate           {@link LocalDate}
     * @param aggregateInterval {@link AggregateInterval}
     * @return {@link Optional} {@link TradeRecord}
     */
    public Optional<TradeRecord> findTradingRecordForStartDateAndEndDateAndInterval(final LocalDate startDate, final LocalDate endDate, final AggregateInterval aggregateInterval) {

        validateParameterIsNotNull(startDate, "start date cannot be null");
        validateParameterIsNotNull(endDate, "end date cannot be null");
        validateParameterIsNotNull(aggregateInterval, "interval cannot be null");
        validateDatesAreNotMutuallyExclusive(startDate.atStartOfDay(), endDate.atStartOfDay(), "start date was after end date or vice versa");

        return Optional.ofNullable(this.tradeRecordRepository.findTradingRecordByStartDateAndEndDateAndAggregateInterval(startDate, endDate, aggregateInterval));
    }

    /**
     * Generates or updates a {@link TradeRecord}
     *
     * @param date              {@link LocalDate}
     * @param aggregateInterval {@link AggregateInterval}
     * @return {@link TradeRecord}
     */
    public TradeRecord generateTradeRecord(final LocalDate date, final AggregateInterval aggregateInterval) {

        validateParameterIsNotNull(date, "date cannot be null");
        validateParameterIsNotNull(aggregateInterval, "interval cannot be null");

        final TradeRecord tradeRecord = findTradingRecordForStartDateAndEndDateAndInterval(date, computeEndDate(date, aggregateInterval), aggregateInterval).orElse(new TradeRecord());
        List<Trade> trades = this.tradeService.findAllTradesWithinDate(date.atStartOfDay(), computeEndDate(date, aggregateInterval).atStartOfDay(), false);

        //  TODO: tie this to the account balance
        double balance = 0.0;
        double profit = this.numberService.getDouble(trades.stream().mapToDouble(Trade::getNetProfit).sum());
        double winingTrades = trades.stream().filter(t -> t.getNetProfit() >= 0.0).count();

        tradeRecord.setNumberOfTrades(trades.size());
        tradeRecord.setEndDate(computeEndDate(date, aggregateInterval));
        tradeRecord.setAggregateInterval(aggregateInterval);
        tradeRecord.setNetProfit(profit);
        tradeRecord.setPercentageProfit(this.numberService.delta(profit, balance));
        tradeRecord.setStartDate(date);
        tradeRecord.setWinPercentage(this.numberService.wholePercentage(winingTrades, trades.size()));
        tradeRecord.setBalance(this.numberService.add(balance, profit));

        //this.tradingRecordRepository.save(tradingRecord);
        return tradeRecord;
    }

    public void processTrades() {

        List<TradeRecord> records = new ArrayList<>();
        List<Trade> notProcessed = this.tradeService.findTradesByProcessed(false);

        if (CollectionUtils.isNotEmpty(notProcessed)) {
            final LocalDate start = notProcessed.get(0).getTradeCloseTime().toLocalDate();
            final LocalDate end = notProcessed.get(notProcessed.size() - 1).getTradeCloseTime().toLocalDate();

            //  DAILY
            LocalDate compare = start;
            while (compare.isBefore(end) || compare.isEqual(end)) {
                records.add(generateTradeRecord(compare, AggregateInterval.DAILY));
                compare = compare.plusDays(1);
            }

            LOGGER.info("Processed {} DAILY records", records.size());
            records.clear();

            //  WEEKLY
            compare = start;
            while (compare.getDayOfWeek() != DayOfWeek.MONDAY) {
                compare = compare.minusDays(1);
            }

            while (compare.isBefore(end) || compare.isEqual(end)) {
                records.add(generateTradeRecord(compare, AggregateInterval.WEEKLY));
                compare = compare.plusWeeks(1);
            }

            LOGGER.info("Processed {} WEEKLY records", records.size());
            records.clear();

            //  MONTHLY
            compare = start.with(TemporalAdjusters.firstDayOfMonth());
            while (compare.isBefore(end) || compare.isEqual(end)) {
                records.add(generateTradeRecord(compare, AggregateInterval.MONTHLY));
                compare = compare.plusMonths(1);
            }

            LOGGER.info("Processed {} MONTHLY records", records.size());
            records.clear();

            //  YEARLY
            compare = start.with(TemporalAdjusters.firstDayOfYear());
            while (compare.isBefore(end) || compare.isEqual(end)) {
                records.add(generateTradeRecord(compare, AggregateInterval.YEARLY));
                compare = compare.plusYears(1);
            }

            LOGGER.info("Processed {} YEARLY records", records.size());
            records.clear();
        }
    }


    //  HELPER

    /**
     * Computes the end date based on the given interval
     *
     * @param localDate {@link LocalDate}
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

                        yield now.minusDays(1);
                    }
                    case MONTHLY -> localDate.plusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).minusDays(1);
                    case YEARLY -> localDate.plusYears(1).with(TemporalAdjusters.firstDayOfYear()).minusDays(1);
                    default -> localDate.plusDays(1);
                };
    }
}
