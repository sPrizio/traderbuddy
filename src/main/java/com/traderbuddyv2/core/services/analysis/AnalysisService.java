package com.traderbuddyv2.core.services.analysis;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.analysis.AnalysisSort;
import com.traderbuddyv2.core.enums.analysis.AnalysisTimeBucket;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecordStatistics;
import com.traderbuddyv2.core.models.nonentities.analysis.bucket.TradeDateBucket;
import com.traderbuddyv2.core.models.nonentities.analysis.bucket.TradeTimeBucket;
import com.traderbuddyv2.core.models.nonentities.analysis.performance.*;
import com.traderbuddyv2.core.models.nonentities.trade.IrrelevantTradeTotals;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.trade.TradeService;
import com.traderbuddyv2.core.services.trade.record.TradeRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
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

    @Resource(name = "tradeRecordService")
    private TradeRecordService tradeRecordService;


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

        averageTradePerformance.setTradingRate(this.tradeRecordService.computeTradingRate(start, end, win));
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

    /**
     * Obtains a {@link List} of {@link TradeRecordPerformanceBucket} which represents a bucket of trade records whose net pips
     * are within a predefined interval for the given time span
     *
     * @param start {@link LocalDate}
     * @param end {@link LocalDate}
     * @param bucketSize size of each bucket
     * @return {@link List} of {@link TradeRecordPerformanceBucket}
     */
    public TradeRecordPerformanceBucketWrapper getWinningDaysBreakdown(final LocalDate start, final LocalDate end, final int bucketSize, final boolean isLoser) {

        validateParameterIsNotNull(start, CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);

        List<TradeRecordPerformanceBucket> buckets = new ArrayList<>();
        List<TradeRecord> tradeRecords = this.tradeRecordService.findHistory(start, end, AggregateInterval.DAILY);
        List<Double> reducedRecords = tradeRecords.stream().map(TradeRecord::getStatistics).map(TradeRecordStatistics::getNetPips).toList();

        double min;
        double compare1;
        double compare2;
        double max;

        if (isLoser) {
            min = reducedRecords.stream().mapToDouble(rec -> rec).min().orElse(0.0);
            compare1 = min;
            compare2 = min + bucketSize;
            max = 0;
        } else {
            min = 0.0;
            compare1 = min;
            compare2 = min + bucketSize;
            max = reducedRecords.stream().mapToDouble(rec -> rec).max().orElse(0.0);
        }

        List<Double> compareRecords;
        while (compare1 < max) {
            compareRecords = pipReduce(reducedRecords, compare1, compare2);
            buckets.add(new TradeRecordPerformanceBucket(this.mathService.getInteger(compare1), this.mathService.getInteger(compare2), compareRecords.size()));

            compare1 += bucketSize;
            compare2 += bucketSize;

            if (isLoser && compare2 > 0.0) {
                compare2 = 0.0;
            }
        }

        if (isLoser) {
            Collections.reverse(buckets);
        }

        double winningAverage = this.mathService.getDouble(tradeRecords.stream().mapToDouble(tr -> tr.getStatistics().getNetPips()).filter(d -> d >= 0.0).average().orElse(0.0));
        double losingAverage = this.mathService.getDouble(tradeRecords.stream().mapToDouble(tr -> tr.getStatistics().getNetPips()).filter(d -> d < 0.0).average().orElse(0.0));
        int winCount = this.mathService.getInteger(tradeRecords.stream().filter(tr -> tr.getStatistics().getNetPips() >= 0.0).count());
        int loseCount = this.mathService.getInteger(tradeRecords.stream().filter(tr -> tr.getStatistics().getNetPips() < 0.0).count());

        return new TradeRecordPerformanceBucketWrapper(buckets, new TradeRecordPerformanceBucketStatistics(isLoser ? losingAverage : winningAverage, isLoser ? loseCount : winCount));
    }

    /**
     * Returns an {@link IrrelevantTradeTotals} for the given start & end date
     *
     * @param start start
     * @param end end
     * @return {@link IrrelevantTradeTotals}
     */
    public IrrelevantTradeTotals getIrrelevantTrades(final LocalDate start, final LocalDate end) {
        final List<Trade> current = this.tradeService.findAllTradesWithinTimespan(start.atStartOfDay(), end.atStartOfDay(), true).stream().filter(trade -> !trade.isRelevant()).toList();
        final List<Trade> previous = this.tradeService.findAllTradesWithinTimespan(start.minusMonths(1).atStartOfDay(), start.atStartOfDay(), true).stream().filter(trade -> !trade.isRelevant()).toList();

        return new IrrelevantTradeTotals(current, previous);
    }

    /**
     * Returns a {@link Map} of each business day and a {@link TradeDateBucket} for each day for the given time span
     *
     * @param start start
     * @param end end
     * @return {@link Map} of {@link DayOfWeek} - {@link TradeDateBucket}
     */
    public Map<DayOfWeek, TradeDateBucket> getTradeDayBuckets(final LocalDate start, final LocalDate end) {

        validateParameterIsNotNull(start, CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start.atStartOfDay(), end.atStartOfDay(), CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);

        final List<Trade> trades = this.tradeService.findAllTradesWithinTimespan(start.atStartOfDay(), end.atStartOfDay(), false);
        final Map<DayOfWeek, TradeDateBucket> map = new EnumMap<>(DayOfWeek.class);

        map.put(DayOfWeek.MONDAY, computeTradeDateBuckets(trades, DayOfWeek.MONDAY));
        map.put(DayOfWeek.TUESDAY, computeTradeDateBuckets(trades, DayOfWeek.TUESDAY));
        map.put(DayOfWeek.WEDNESDAY, computeTradeDateBuckets(trades, DayOfWeek.WEDNESDAY));
        map.put(DayOfWeek.THURSDAY, computeTradeDateBuckets(trades, DayOfWeek.THURSDAY));
        map.put(DayOfWeek.FRIDAY, computeTradeDateBuckets(trades, DayOfWeek.FRIDAY));

        return map;
    }


    //  HELPERS

    /**
     * Computes a {@link TradeDateBucket} for the given {@link List} of {@link Trade}s and {@link DayOfWeek}
     *
     * @param trades {@link List} of {@link Trade}s
     * @param dayOfWeek {@link DayOfWeek}
     * @return {@link TradeDateBucket}
     */
    private TradeDateBucket computeTradeDateBuckets(final List<Trade> trades, final DayOfWeek dayOfWeek) {

        if (CollectionUtils.isEmpty(trades) || dayOfWeek == null || (dayOfWeek == DayOfWeek.SATURDAY || dayOfWeek == DayOfWeek.SUNDAY)) {
            return null;
        }

        final List<Trade> filtered =
                trades
                        .stream()
                        .filter(t -> t.getTradeOpenTime().getDayOfWeek() == dayOfWeek)
                        .toList();

        return new TradeDateBucket(null, null, filtered);
    }

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

    /**
     * Filters a {@link List} based on 2 compare values
     *
     * @param reducedRecords {@link List} of {@link Double}
     * @param compare1 first compare
     * @param compare2 second compare
     * @return {@link List} of filtered {@link Double}
     */
    private List<Double> pipReduce(final List<Double> reducedRecords, final double compare1, final double compare2) {
        return reducedRecords.stream().filter(d -> d >= compare1).filter(d -> d < compare2).toList();
    }
}
