package com.stephenprizio.traderbuddy.services.summary;

import com.stephenprizio.traderbuddy.enums.trades.TradingSummaryInterval;
import com.stephenprizio.traderbuddy.models.entities.trades.Trade;
import com.stephenprizio.traderbuddy.models.records.reporting.TradingRecord;
import com.stephenprizio.traderbuddy.models.records.reporting.TradingRecordStatistics;
import com.stephenprizio.traderbuddy.models.records.reporting.TradingSummary;
import com.stephenprizio.traderbuddy.repositories.trades.TradeRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateParameterIsNotNull;

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

    //  TODO: will need the use of a Goal / Target
    /**
     * Obtains a {@link TradingRecord} for the given time interval
     *
     * @param start start of time span
     * @param end end of time span
     * @return {@link TradingRecord}
     */
    public TradingRecord getSummaryForTimeSpan(final LocalDateTime start, final LocalDateTime end) {

        validateParameterIsNotNull(start, "startDate cannot be null");
        validateParameterIsNotNull(end, "endDate cannot be null");
        validateDatesAreNotMutuallyExclusive(start, end, "startDate was after endDate or vice versa");

        List<Trade> trades = this.tradeRepository.findAllTradesWithinDate(start, end).stream().filter(Trade::getRelevant).toList();
        BigDecimal netProfit = BigDecimal.valueOf(trades.stream().mapToDouble(Trade::getNetProfit).sum()).setScale(2, RoundingMode.HALF_EVEN);
        double winningTrades = trades.stream().filter(t -> t.getNetProfit() > 0.0).toList().size();

        BigDecimal winPercentage = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(trades)) {
            winPercentage =
                    BigDecimal.valueOf(winningTrades).setScale(2, RoundingMode.HALF_EVEN)
                            .divide(BigDecimal.valueOf(trades.size()), RoundingMode.HALF_EVEN)
                            .multiply(BigDecimal.valueOf(100.0));
        }

        return new TradingRecord(start, 0.0, trades.size(), winPercentage.setScale(0, RoundingMode.HALF_EVEN).intValue(), netProfit.doubleValue(), 0.0, /*netProfit.subtract(BigDecimal.valueOf(target)).doubleValue()*/0.0);
    }

    //  TODO: will need the use of a Goal / Target
    /**
     * Generates a {@link TradingSummary} for the given time span and interval
     *
     * @param start start of time span
     * @param end end of time span
     * @param interval {@link TradingSummaryInterval}
     * @return {@link TradingSummary}
     */
    public TradingSummary getReportOfSummariesForTimeSpan(final LocalDateTime start, final LocalDateTime end, final TradingSummaryInterval interval) {

        validateParameterIsNotNull(start, "startDate cannot be null");
        validateParameterIsNotNull(end, "endDate cannot be null");
        validateParameterIsNotNull(interval, "interval cannot be null");
        validateDatesAreNotMutuallyExclusive(start, end, "startDate was after endDate or vice versa");

        List<TradingRecord> records = new ArrayList<>();
        LocalDateTime leftBound = interval == TradingSummaryInterval.YEARLY ? start.with(TemporalAdjusters.firstDayOfYear()) : start.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime rightBound = getNextDate(leftBound, interval);

        do {
            records.add(getSummaryForTimeSpan(leftBound, rightBound));
            leftBound = getNextDate(leftBound, interval);
            rightBound = getNextDate(rightBound, interval);
        } while (interval == TradingSummaryInterval.MONTHLY ? rightBound.isBefore(end.plusMonths(1)) : (rightBound.isBefore(end) || rightBound.isEqual(end)));

        return new TradingSummary(records, new TradingRecordStatistics(records));
    }


    //  HELPERS

    /**
     * Obtains the next date for the given {@link TradingSummaryInterval}
     *
     * @param compare {@link LocalDateTime}
     * @param interval {@link TradingSummaryInterval}
     * @return offset {@link LocalDateTime}
     */
    private LocalDateTime getNextDate(final LocalDateTime compare, final TradingSummaryInterval interval) {

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
}
