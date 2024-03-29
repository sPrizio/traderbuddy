package com.traderbuddyv2.core.services.trade;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.trade.info.TradeType;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import com.traderbuddyv2.core.repositories.trade.TradeRepository;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import com.traderbuddyv2.integration.models.dto.eod.IntradayHistoricalDataDTO;
import com.traderbuddyv2.integration.models.dto.eod.TradePointDTO;
import com.traderbuddyv2.integration.services.eod.EODIntegrationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.List;
import java.util.Optional;

import static com.traderbuddyv2.core.validation.GenericValidator.*;

/**
 * Service-layer for {@link Trade}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("tradeService")
public class TradeService {

    @Resource(name = "eodIntegrationService")
    private EODIntegrationService eodIntegrationService;

    @Resource(name = "tradeRepository")
    private TradeRepository tradeRepository;

    @Resource(name = "traderBuddyUserDetailsService")
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;


    //  METHODS

    /**
     * Returns a {@link List} of {@link Trade}s for the given {@link TradeType}
     *
     * @param tradeType          {@link TradeType}
     * @param includeNonRelevant flag to return non-active/relevant trades
     * @return {@link List} of {@link Trade}s
     */
    public List<Trade> findAllByTradeType(final TradeType tradeType, final boolean includeNonRelevant) {

        validateParameterIsNotNull(tradeType, "tradeType cannot be null");

        if (!includeNonRelevant) {
            return this.tradeRepository.findAllByTradeTypeAndAccountOrderByTradeOpenTimeAsc(tradeType, this.traderBuddyUserDetailsService.getCurrentUser().getAccount()).stream().filter(Trade::isRelevant).toList();
        }

        return this.tradeRepository.findAllByTradeTypeAndAccountOrderByTradeOpenTimeAsc(tradeType, this.traderBuddyUserDetailsService.getCurrentUser().getAccount());
    }

    /**
     * Returns a {@link List} of {@link Trade}s that are within the given time span
     *
     * @param start              {@link LocalDateTime} start of interval (inclusive)
     * @param end                {@link LocalDateTime} end of interval (exclusive)
     * @param includeNonRelevant flag to return non-active/relevant trades
     * @return {@link List} of {@link Trade}s
     */
    public List<Trade> findAllTradesWithinTimespan(final LocalDateTime start, final LocalDateTime end, final boolean includeNonRelevant) {

        validateParameterIsNotNull(start, CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start, end, CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);

        //  sometimes trades can be opened on 1 day and closed on another, we use the start day as the source of truth
        if (!includeNonRelevant) {
            return this.tradeRepository.findAllTradesWithinDate(start, start.plusYears(1).toLocalDate().atStartOfDay(), this.traderBuddyUserDetailsService.getCurrentUser().getAccount()).stream().filter(Trade::isRelevant).toList();
        }

        return this.tradeRepository.findAllTradesWithinDate(start, start.plusYears(1).toLocalDate().atStartOfDay(), this.traderBuddyUserDetailsService.getCurrentUser().getAccount());
    }

    /**
     * Returns a {@link List} of {@link Trade}s that are within the given time span
     *
     * @param start              {@link LocalDateTime} start of interval (inclusive)
     * @param end                {@link LocalDateTime} end of interval (exclusive)
     * @param includeNonRelevant flag to return non-active/relevant trades
     * @param page               page number
     * @param pageSize           page size
     * @return {@link List} of {@link Trade}s
     */
    public Page<Trade> findAllTradesWithinTimespan(final LocalDateTime start, final LocalDateTime end, final boolean includeNonRelevant, final int page, final int pageSize) {

        validateParameterIsNotNull(start, CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start, end, CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);

        //  sometimes trades can be opened on 1 day and closed on another, we use the start day as the source of truth
        if (!includeNonRelevant) {
            return this.tradeRepository.findAllRelevantTradesWithinDate(start, end.toLocalDate().atStartOfDay(), this.traderBuddyUserDetailsService.getCurrentUser().getAccount(), PageRequest.of(page, pageSize));
        }

        return this.tradeRepository.findAllTradesWithinDate(start, end.toLocalDate().atStartOfDay(), this.traderBuddyUserDetailsService.getCurrentUser().getAccount(), PageRequest.of(page, pageSize));
    }

    /**
     * Returns a {@link List} of {@link Trade}s that are within the {@link TradeRecord}
     *
     * @param tradeRecord {@link TradeRecord}
     * @return {@link List} of {@link Trade}s
     */
    public List<Trade> findAllTradesForTradeRecord(final TradeRecord tradeRecord) {
        validateParameterIsNotNull(tradeRecord, CoreConstants.Validation.TRADE_RECORD_CANNOT_BE_NULL);
        return this.tradeRepository.findAllTradesForTradeRecord(tradeRecord.getStartDate().atStartOfDay(), tradeRecord.getEndDate().atStartOfDay(), this.traderBuddyUserDetailsService.getCurrentUser().getAccount());
    }

    /**
     * Returns an {@link Optional} containing a {@link Trade}
     *
     * @param tradeId trade id
     * @return {@link Optional} {@link Trade}
     */
    public Optional<Trade> findTradeByTradeId(final String tradeId) {
        validateParameterIsNotNull(tradeId, "tradeId cannot be null");

        return Optional.ofNullable(this.tradeRepository.findTradeByTradeIdAndAccount(tradeId, this.traderBuddyUserDetailsService.getCurrentUser().getAccount()));
    }

    /**
     * Returns a {@link List} of {@link Trade}s by their processed flag
     *
     * @param processed processed flag
     * @return {@link List} of {@link Trade}
     */
    public List<Trade> findTradesByProcessed(final boolean processed) {
        validateParameterIsNotNull(processed, "processed cannot be null");
        return this.tradeRepository.findAllByProcessedAndAccountAndRelevantIsTrueOrderByTradeOpenTimeAsc(processed, this.traderBuddyUserDetailsService.getCurrentUser().getAccount());
    }

    /**
     * Finds the trade recap data for a trade with the given trade id
     *
     * @param tradeId {@link Trade} trade id
     * @return {@link IntradayHistoricalDataDTO}
     */
    public IntradayHistoricalDataDTO findTradeRecap(final String tradeId) {

        Optional<Trade> trade = findTradeByTradeId(tradeId);
        validateIfPresent(trade, "No trade was found with trade id: %s", tradeId);

        IntradayHistoricalDataDTO recap = this.eodIntegrationService.getIntradayData("NDX.INDX", "5m", trade.get().getTradeCloseTime().with(ChronoField.NANO_OF_DAY, LocalTime.MIN.toNanoOfDay()), trade.get().getTradeCloseTime().with(ChronoField.NANO_OF_DAY, LocalTime.MAX.toNanoOfDay()));
        recap.setPoints(List.of(new TradePointDTO(trade.get(), true), new TradePointDTO(trade.get(), false)));

        return recap;
    }

    /**
     * Processes a {@link Trade}. This means that a trades information has been captured by the system and is now considered complete.
     *
     * @param trade {@link Trade}
     */
    public void processTrade(final Trade trade) {
        trade.setProcessed(true);
        this.tradeRepository.save(trade);
    }

    /**
     * Will mark a {@link Trade} as not relevant to exclude it from consideration
     *
     * @param tradeId {@link Trade}'s unique trade id
     * @return true if successful operation
     */
    public boolean disregardTrade(final String tradeId) {

        validateParameterIsNotNull(tradeId, "tradeId cannot be null");

        Optional<Trade> trade = Optional.ofNullable(this.tradeRepository.findTradeByTradeIdAndAccount(tradeId, this.traderBuddyUserDetailsService.getCurrentUser().getAccount()));
        if (trade.isPresent()) {
            final Trade found = trade.get();
            found.setRelevant(false);
            this.tradeRepository.save(found);

            return true;
        }

        return false;
    }
}
