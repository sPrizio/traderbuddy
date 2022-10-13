package com.stephenprizio.traderbuddy.services.trades;

import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import com.stephenprizio.traderbuddy.enums.trades.TradeType;
import com.stephenprizio.traderbuddy.models.entities.trades.Trade;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingRecord;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingSummary;
import com.stephenprizio.traderbuddy.repositories.trades.TradeRepository;
import com.stephenprizio.traderbuddy.services.summary.TradingSummaryService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link Trade}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("tradeService")
public class TradeService {

    @Resource(name = "tradeRepository")
    private TradeRepository tradeRepository;

    @Resource(name = "tradingSummaryService")
    private TradingSummaryService tradingSummaryService;


    //  METHODS

    /**
     * Returns a {@link List} of {@link Trade}s for the given {@link TradeType}
     *
     * @param tradeType {@link TradeType}
     * @param includeNonRelevant flag to return non-active/relevant trades
     * @return {@link List} of {@link Trade}s
     */
    public List<Trade> findAllByTradeType(final TradeType tradeType, final Boolean includeNonRelevant) {

        validateParameterIsNotNull(tradeType, "tradeType cannot be null");

        if (Boolean.FALSE.equals(includeNonRelevant)) {
            return this.tradeRepository.findAllByTradeTypeOrderByTradeOpenTimeAsc(tradeType).stream().filter(Trade::getRelevant).toList();
        }

        return this.tradeRepository.findAllByTradeTypeOrderByTradeOpenTimeAsc(tradeType);
    }

    /**
     * Returns a {@link List} of {@link Trade}s that are within the given time span
     *
     * @param start {@link LocalDateTime} start of interval (inclusive)
     * @param end {@link LocalDateTime} end of interval (exclusive)
     * @param includeNonRelevant flag to return non-active/relevant trades
     * @return {@link List} of {@link Trade}s
     */
    public List<Trade> findAllTradesWithinDate(final LocalDateTime start, final LocalDateTime end, final Boolean includeNonRelevant) {

        validateParameterIsNotNull(start, "startDate cannot be null");
        validateParameterIsNotNull(end, "endDate cannot be null");
        validateDatesAreNotMutuallyExclusive(start, end, "startDate was after endDate or vice versa");

        if (Boolean.FALSE.equals(includeNonRelevant)) {
            return this.tradeRepository.findAllTradesWithinDate(start, end).stream().filter(Trade::getRelevant).toList();
        }

        return this.tradeRepository.findAllTradesWithinDate(start, end);
    }

    /**
     * Returns an {@link Optional} containing a {@link Trade}
     *
     * @param tradeId trade id
     * @return {@link Optional} {@link Trade}
     */
    public Optional<Trade> findTradeByTradeId(final String tradeId) {
        validateParameterIsNotNull(tradeId, "tradeId cannot be null");

        return Optional.ofNullable(this.tradeRepository.findTradeByTradeId(tradeId));
    }

    /**
     * Returns a {@link List} of the most recent N {@link TradingRecord}s
     *
     * @param count limit size
     * @return {@link List} of {@link TradingRecord}s
     */
    public List<TradingRecord> findRecentTrades(int count) {

        Stream<TradingRecord> summary =
                this.tradingSummaryService.getReportOfSummariesForTimeSpan(LocalDateTime.now().minusMonths(3).with(TemporalAdjusters.firstDayOfMonth()), LocalDateTime.now().plusDays(1).toLocalDate().atStartOfDay(), AggregateInterval.DAILY)
                        .records()
                        .stream()
                        .filter(TradingRecord::isNotEmpty)
                        .sorted(Comparator.comparing(TradingRecord::start).reversed());

        if (count == -1) {
            return summary.toList();
        }

        return summary.limit(count).toList();
    }

    /**
     * Will mark a {@link Trade} as not relevant to exclude it from consideration
     *
     * @param tradeId {@link Trade}'s unique trade id
     * @return true if successful operation
     */
    public Boolean disregardTrade(final String tradeId) {

        validateParameterIsNotNull(tradeId, "tradeId cannot be null");

        Optional<Trade> trade = Optional.ofNullable(this.tradeRepository.findTradeByTradeId(tradeId));
        if (trade.isPresent()) {
            final Trade found = trade.get();
            found.setRelevant(false);
            this.tradeRepository.save(found);

            return true;
        }

        return false;
    }
}
