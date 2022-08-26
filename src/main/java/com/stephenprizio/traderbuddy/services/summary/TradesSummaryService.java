package com.stephenprizio.traderbuddy.services.summary;

import com.stephenprizio.traderbuddy.enums.TradesSummaryInterval;
import com.stephenprizio.traderbuddy.models.entities.Trade;
import com.stephenprizio.traderbuddy.repositories.TradeRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;

import static com.stephenprizio.traderbuddy.validation.TraderBuddyValidator.validateDatesAreNotMutuallyExclusive;
import static com.stephenprizio.traderbuddy.validation.TraderBuddyValidator.validateParameterIsNotNull;

/**
 * Service for proving summaries and reports of trades for time spans and intervals
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("tradesSummaryService")
public class TradesSummaryService {

    private final TradeRepository tradeRepository;

    @Autowired
    public TradesSummaryService(TradeRepository tradeRepository) {
        this.tradeRepository = tradeRepository;
    }


    //  METHODS

    //  TODO: will need the use of a Goal / Target
    /**
     * Obtains a {@link TradeSummary} for the given time interval
     *
     * @param start start of time span
     * @param end end of time span
     * @return {@link TradeSummary}
     */
    public TradeSummary getSummaryForTimeSpan(LocalDateTime start, LocalDateTime end) {
        validateParameterIsNotNull(start, "startDate cannot be null");
        validateParameterIsNotNull(end, "endDate cannot be null");
        validateDatesAreNotMutuallyExclusive(start, end, "startDate was after endDate or vice versa");

        List<Trade> trades = this.tradeRepository.findAllTradesWithinDate(start, end);
        BigDecimal netProfit = BigDecimal.valueOf(trades.stream().mapToDouble(Trade::getNetProfit).sum()).setScale(2, RoundingMode.HALF_EVEN);
        double winningTrades = trades.stream().filter(t -> t.getNetProfit() > 0.0).toList().size();

        BigDecimal winPercentage = BigDecimal.ZERO;
        if (CollectionUtils.isNotEmpty(trades)) {
            winPercentage =
                    BigDecimal.valueOf(winningTrades)
                            .divide(BigDecimal.valueOf(trades.size()), RoundingMode.HALF_EVEN)
                            .multiply(BigDecimal.valueOf(100.0));
        }

        return new TradeSummary(start, 0.0, trades.size(), winPercentage.setScale(0, RoundingMode.HALF_EVEN).intValue(), netProfit.doubleValue(), 0.0, /*netProfit.subtract(BigDecimal.valueOf(target)).doubleValue()*/0.0);
    }

    //  TODO: will need the use of a Goal / Target
    /**
     * Generates a {@link List} of {@link TradeSummary} for the given time span and interval
     *
     * @param start start of time span
     * @param end end of time span
     * @param interval {@link TradesSummaryInterval}
     * @return {@link List} of {@link TradeSummary}
     */
    public List<TradeSummary> getReportOfSummariesForTimeSpan(LocalDateTime start, LocalDateTime end, TradesSummaryInterval interval) {
        validateParameterIsNotNull(start, "startDate cannot be null");
        validateParameterIsNotNull(end, "endDate cannot be null");
        validateParameterIsNotNull(interval, "interval cannot be null");
        validateDatesAreNotMutuallyExclusive(start, end, "startDate was after endDate or vice versa");

        List<TradeSummary> summary = new ArrayList<>();
        LocalDateTime leftBound = interval == TradesSummaryInterval.YEARLY ? start.with(TemporalAdjusters.firstDayOfYear()) : start.with(TemporalAdjusters.firstDayOfMonth());
        LocalDateTime rightBound = getNextDate(leftBound, interval);

        do {
            summary.add(getSummaryForTimeSpan(leftBound, rightBound));
            leftBound = getNextDate(leftBound, interval);
            rightBound = getNextDate(rightBound, interval);
        } while (interval == TradesSummaryInterval.MONTHLY ? rightBound.isBefore(end.plusMonths(1)) : rightBound.isBefore(end));

        return summary;
    }


    //  HELPERS

    /**
     * Obtains the next date for the given {@link TradesSummaryInterval}
     *
     * @param compare {@link LocalDateTime}
     * @param interval {@link TradesSummaryInterval}
     * @return offset {@link LocalDateTime}
     */
    private LocalDateTime getNextDate(LocalDateTime compare, TradesSummaryInterval interval) {
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


    //  RECORDS

    /**
     * Represents a summary of trades
     *
     * @param date {@link LocalDateTime} of period
     * @param target target goal for profit
     * @param numberOfTrades number of trades taken
     * @param winPercentage number of trades won expressed as a percentage between 0 & 100
     * @param netProfit net profit of trades (can be negative)
     * @param percentageProfit profit as a percentage of target
     * @param surplus net difference between target and profit
     */
    private record TradeSummary(LocalDateTime date, Double target, Integer numberOfTrades, Integer winPercentage, Double netProfit, Double percentageProfit, Double surplus) {}
}
