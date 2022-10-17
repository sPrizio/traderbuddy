package com.stephenprizio.traderbuddy.services.summary;

import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import com.stephenprizio.traderbuddy.models.entities.trades.Trade;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingRecord;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingRecordStatistics;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingSummary;
import com.stephenprizio.traderbuddy.repositories.trades.TradeRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.*;

/**
 * Service for proving summaries and reports of trades for time spans and intervals
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("tradingSummaryService")
public class TradingSummaryService {

    private final TradeRepository tradeRepository;

    @Autowired
    public TradingSummaryService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }


    //  METHODS

    /**
     * Obtains a {@link TradingRecord} for the given time interval
     *
     * @param start start of time span
     * @param end   end of time span
     * @return {@link TradingRecord}
     */
    public TradingRecord getSummaryForTimeSpan(final LocalDateTime start, final LocalDateTime end) {

        validateParameterIsNotNull(start, "startDate cannot be null");
        validateParameterIsNotNull(end, "endDate cannot be null");
        validateDatesAreNotMutuallyExclusive(start, end, "startDate was after endDate or vice versa");

        List<Trade> trades = this.tradeRepository.findAllTradesWithinDate(start, end).stream().filter(Trade::getRelevant).toList();
        return new TradingRecord(
                trades,
                start,
                end,
                0.0,
                0.0,
                0.0,
                ChronoUnit.DAYS.between(start, end) > 1,
                null);
    }

    /**
     * Generates a {@link TradingSummary} for the given time span and interval
     *
     * @param start    start of time span
     * @param end      end of time span
     * @param interval {@link AggregateInterval}
     * @return {@link TradingSummary}
     */
    public TradingSummary getReportOfSummariesForTimeSpan(final LocalDateTime start, final LocalDateTime end, final AggregateInterval interval) {

        validateParameterIsNotNull(start, "startDate cannot be null");
        validateParameterIsNotNull(end, "endDate cannot be null");
        validateParameterIsNotNull(interval, "interval cannot be null");
        validateDatesAreNotMutuallyExclusive(start, end, "startDate was after endDate or vice versa");

        List<Trade> allTrades = this.tradeRepository.findAllTradesWithinDate(start, end);
        LocalDateTime earliestTradeTime = start;
        LocalDateTime mostRecentTradeTime = end;

        if (CollectionUtils.isNotEmpty(allTrades)) {
            earliestTradeTime = computeEarliestTradeTime(start, allTrades, interval);
            mostRecentTradeTime = computeMostRecentTradeTime(end, allTrades, interval);
        }

        List<TradingRecord> records = new ArrayList<>();
        LocalDateTime leftBound = interval == AggregateInterval.YEARLY ? earliestTradeTime.with(TemporalAdjusters.firstDayOfYear()) : earliestTradeTime.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime rightBound = getNextDate(leftBound, interval);

        do {
            records.add(getSummaryForTimeSpan(leftBound, rightBound));
            leftBound = getNextDate(leftBound, interval);
            rightBound = getNextDate(rightBound, interval);
        } while (interval == AggregateInterval.MONTHLY ? rightBound.isBefore(mostRecentTradeTime.plusMonths(1)) : (rightBound.isBefore(mostRecentTradeTime) || rightBound.isEqual(mostRecentTradeTime)));

        return new TradingSummary(records, new TradingRecordStatistics(records));
    }

    /**
     * Returns {@link TradingRecordStatistics} for the given {@link Month} & year
     *
     * @param month {@link Month}
     * @param year calendar year
     * @return {@link TradingRecordStatistics}
     */
    public TradingRecordStatistics getStatisticsForMonthAndYear(final Month month, final int year) {

        validateParameterIsNotNull(month, "month cannot be null");
        validateAcceptableYear(year, StringUtils.EMPTY);

        LocalDate start = LocalDate.of(year, month, 1);
        TradingSummary summary = getReportOfSummariesForTimeSpan(start.atStartOfDay(), start.plusMonths(1).atStartOfDay(), AggregateInterval.DAILY);
        return summary.statistics();
    }


    //  HELPERS

    /**
     * Obtains the next date for the given {@link AggregateInterval}
     *
     * @param compare  {@link LocalDateTime}
     * @param interval {@link AggregateInterval}
     * @return offset {@link LocalDateTime}
     */
    private LocalDateTime getNextDate(final LocalDateTime compare, final AggregateInterval interval) {

        LocalDateTime result;
        switch (interval) {
            case DAILY -> result = compare.plusDays(1);
            case WEEKLY -> result = compare.plusWeeks(1);
            case MONTHLY -> result = compare.plusMonths(1);
            case YEARLY -> result = compare.plusYears(1);
            default -> result = LocalDateTime.MIN;
        }

        return result;
    }

    /**
     * Computes the earliest trade time based on the given time interval
     *
     * @param test     {@link LocalDateTime}
     * @param trades   {@link List} of {@link Trade}
     * @param interval {@link AggregateInterval}
     * @return {@link LocalDateTime}
     */
    private LocalDateTime computeEarliestTradeTime(final LocalDateTime test, final List<Trade> trades, final AggregateInterval interval) {
        return switch (interval) {
            case DAILY, WEEKLY, MONTHLY ->
                    (test.isAfter(trades.get(0).getTradeOpenTime()) ? test : trades.get(0).getTradeOpenTime()).with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
            case YEARLY ->
                    (test.isAfter(trades.get(0).getTradeOpenTime()) ? test : trades.get(0).getTradeOpenTime()).with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
        };
    }

    /**
     * Computes the most recent trade time based on the given time interval
     *
     * @param test     {@link LocalDateTime}
     * @param trades   {@link List} of {@link Trade}
     * @param interval {@link AggregateInterval}
     * @return {@link LocalDateTime}
     */
    private LocalDateTime computeMostRecentTradeTime(final LocalDateTime test, final List<Trade> trades, final AggregateInterval interval) {
        return
                switch (interval) {
                    case DAILY, WEEKLY, MONTHLY ->
                            (test.isBefore(trades.get(trades.size() - 1).getTradeOpenTime()) ? test : trades.get(trades.size() - 1).getTradeOpenTime()).plusMonths(1).with(TemporalAdjusters.firstDayOfMonth()).with(LocalTime.MIN);
                    case YEARLY ->
                            (test.isBefore(trades.get(trades.size() - 1).getTradeOpenTime()) ? test : trades.get(trades.size() - 1).getTradeOpenTime()).plusYears(1).with(TemporalAdjusters.firstDayOfYear()).with(LocalTime.MIN);
                };
    }
}
