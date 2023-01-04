package com.traderbuddyv2.core.services.analysis;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.analysis.AnalysisSort;
import com.traderbuddyv2.core.enums.analysis.AnalysisTimeBucket;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.models.nonentities.analysis.AverageTradePerformance;
import com.traderbuddyv2.core.models.nonentities.analysis.TradePerformance;
import com.traderbuddyv2.core.models.nonentities.analysis.TradeTimeBucket;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.trade.TradeService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static com.traderbuddyv2.core.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for analyzing trades and performances for insights
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("analysisService")
public class AnalysisService {

    @Resource(name = "mathService")
    private MathService mathService;

    @Resource(name = "tradeService")
    private TradeService tradeService;


    //  METHODS

    /**
     * Obtains the top performance for {@link Trade}s within the given time span
     *
     * @param start        {@link LocalDate} start
     * @param end          {@link LocalDate} end
     * @param sort         {@link AnalysisSort}
     * @param sortByLosses flag to sort by wins or losses
     * @param count        size limit
     * @return {@link List} of {@link TradePerformance}
     */
    public List<TradePerformance> getTopTradePerformance(final LocalDate start, final LocalDate end, final AnalysisSort sort, final boolean sortByLosses, final int count) {

        validateParameterIsNotNull(start, CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start.atStartOfDay(), end.atStartOfDay(), CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);
        validateParameterIsNotNull(sort, "sort cannot be null");

        final Comparator<TradePerformance> performanceSort = sortByLosses ? sort.getSort() : sort.getSort().reversed();
        final Predicate<Trade> profitFilter = sortByLosses ? trade -> trade.getNetProfit() < 0 : trade -> trade.getNetProfit() >= 0;
        final List<Trade> trades = this.tradeService.findAllTradesWithinTimespan(start.atStartOfDay(), end.atStartOfDay(), false);
        final int trueLimit = count == (CoreConstants.MAX_RESULT_SIZE) ? 1000000 : count;

        if (CollectionUtils.isNotEmpty(trades)) {
            return trades
                    .stream()
                    .filter(profitFilter)
                    .map(TradePerformance::new)
                    .sorted(performanceSort)
                    .limit(trueLimit)
                    .toList();
        }

        return Collections.emptyList();
    }

    /**
     * Obtains the average performance for a calendar period
     *
     * @param start {@link LocalDate} start
     * @param end   {@link LocalDate} end
     * @param win   filter by wins or losses
     * @param count size limit
     * @return {@link AverageTradePerformance}
     */
    public AverageTradePerformance getAverageTradePerformance(final LocalDate start, final LocalDate end, final boolean win, final int count) {

        final AverageTradePerformance averageTradePerformance = new AverageTradePerformance();
        final List<TradePerformance> winningPerformances = getTopTradePerformance(start, end, AnalysisSort.CLOSE_TIME, false, count);
        final List<TradePerformance> losingPerformances = getTopTradePerformance(start, end, AnalysisSort.CLOSE_TIME, true, count);
        final List<TradePerformance> tradePerformances = win ? winningPerformances : losingPerformances;

        final int size = winningPerformances.size() + losingPerformances.size();
        final double winningPips = winningPerformances.stream().mapToDouble(TradePerformance::getPips).sum();
        final double losingPips = Math.abs(losingPerformances.stream().mapToDouble(TradePerformance::getPips).sum());

        averageTradePerformance.setTradingRate(this.mathService.divide(tradePerformances.size(), ChronoUnit.DAYS.between(start, end)));
        averageTradePerformance.setAveragePips(this.mathService.getDouble(tradePerformances.stream().mapToDouble(TradePerformance::getPips).average().orElse(0.0)));
        averageTradePerformance.setAverageTradeDuration((long) tradePerformances.stream().mapToLong(TradePerformance::getTradeDuration).average().orElse(0.0));
        averageTradePerformance.setAverageProfit(this.mathService.getDouble(tradePerformances.stream().mapToDouble(TradePerformance::getNetProfit).average().orElse(0.0)));
        averageTradePerformance.setAverageLotSize(this.mathService.getDouble(tradePerformances.stream().mapToDouble(TradePerformance::getLotSize).average().orElse(0.0)));
        averageTradePerformance.setNumberOfWinLosses(tradePerformances.size());
        averageTradePerformance.setWinLossPercentage(this.mathService.wholePercentage(tradePerformances.size(), size));
        averageTradePerformance.setTotalTrades(size);
        averageTradePerformance.setTotalAverageDuration(this.mathService.getInteger(Stream.concat(winningPerformances.stream(), losingPerformances.stream()).mapToLong(TradePerformance::getTradeDuration).average().orElse(0.0)));
        averageTradePerformance.setProfitability(this.mathService.divide(winningPips, losingPips));
        averageTradePerformance.setTotalPips(this.mathService.getDouble(tradePerformances.stream().mapToDouble(TradePerformance::getPips).sum()));

        return averageTradePerformance;
    }

    /**
     * Obtains a {@link List} of {@link TradeTimeBucket}s
     *
     * @param start {@link LocalDate}
     * @param end {@link LocalDate}
     * @param bucket {@link AnalysisTimeBucket}
     * @return {@link List} of {@link TradeTimeBucket}s
     */
    public List<TradeTimeBucket> getTradeBuckets(final LocalDate start, final LocalDate end, final AnalysisTimeBucket bucket) {

        validateParameterIsNotNull(start, CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start.atStartOfDay(), end.atStartOfDay(), CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);
        validateParameterIsNotNull(bucket, "bucket cannot be null");

        final List<TradeTimeBucket> buckets = new ArrayList<>();
        final List<Trade> trades = this.tradeService.findAllTradesWithinTimespan(start.atStartOfDay(), end.atStartOfDay(), false);

        LocalTime compare = LocalTime.of(9, 30);
        while ((compare.isBefore(LocalTime.of(16, 5)))) {
            LocalTime temp = compare;
            final TradeTimeBucket tradeTimeBucket = new TradeTimeBucket(temp, temp.plusMinutes(bucket.getMinutes()), trades.stream().filter(trade -> compareTradeTimes(trade, temp, bucket)).toList());
            tradeTimeBucket.setWinPercentage(this.mathService.wholePercentage(tradeTimeBucket.getWinningTrades(), tradeTimeBucket.getTrades()));
            buckets.add(tradeTimeBucket);
            compare = compare.plusMinutes(bucket.getMinutes());
        }

        return buckets.stream().filter(b -> b.getTrades() > 0).toList();
    }


    //  HELPERS

    /**
     * Compares {@link Trade} close times to be within the bucket
     *
     * @param trade {@link Trade}
     * @param compare compare date
     * @param bucket {@link AnalysisTimeBucket}
     * @return true if the {@link Trade} close time is within the bucket bounds
     */
    private boolean compareTradeTimes(final Trade trade, final LocalTime compare, final AnalysisTimeBucket bucket) {
        return (trade.getTradeCloseTime().toLocalTime().isAfter(compare)) && (trade.getTradeCloseTime().toLocalTime().isBefore(compare.plusMinutes(bucket.getMinutes())));
    }
}
