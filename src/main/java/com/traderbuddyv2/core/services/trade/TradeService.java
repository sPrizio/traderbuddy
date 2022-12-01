package com.traderbuddyv2.core.services.trade;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.trades.TradeType;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.repositories.trade.TradeRepository;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.traderbuddyv2.core.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;

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

        if (!includeNonRelevant) {
            return this.tradeRepository.findAllTradesWithinDate(start, end, this.traderBuddyUserDetailsService.getCurrentUser().getAccount()).stream().filter(Trade::isRelevant).toList();
        }

        return this.tradeRepository.findAllTradesWithinDate(start, end, this.traderBuddyUserDetailsService.getCurrentUser().getAccount());
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

        if (!includeNonRelevant) {
            return this.tradeRepository.findAllRelevantTradesWithinDate(start, end, this.traderBuddyUserDetailsService.getCurrentUser().getAccount(), PageRequest.of(page, pageSize));
        }

        return this.tradeRepository.findAllTradesWithinDate(start, end, this.traderBuddyUserDetailsService.getCurrentUser().getAccount(), PageRequest.of(page, pageSize));
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
        return this.tradeRepository.findAllByProcessedAndAccountAndRelevantIsTrue(processed, this.traderBuddyUserDetailsService.getCurrentUser().getAccount());
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
