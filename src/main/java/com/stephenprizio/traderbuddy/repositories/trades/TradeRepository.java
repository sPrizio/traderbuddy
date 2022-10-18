package com.stephenprizio.traderbuddy.repositories.trades;

import com.stephenprizio.traderbuddy.enums.trades.TradeType;
import com.stephenprizio.traderbuddy.models.entities.trades.Trade;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repository for {@link Trade}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface TradeRepository extends PagingAndSortingRepository<Trade, Long> {

    /**
     * Returns a {@link List} of {@link Trade}s for the given {@link TradeType}
     *
     * @param tradeType {@link TradeType}
     * @return {@link List} of {@link Trade}s
     */
    List<Trade> findAllByTradeTypeOrderByTradeOpenTimeAsc(final TradeType tradeType);

    /**
     * Returns a {@link List} of {@link Trade}s that are within the given time span
     *
     * @param start {@link LocalDateTime} start of interval (inclusive)
     * @param end {@link LocalDateTime} end of interval (exclusive)
     * @return {@link List} of {@link Trade}s
     */
    @Query("SELECT t FROM Trade t WHERE t.tradeOpenTime >= ?1 AND t.tradeCloseTime < ?2 ORDER BY t.tradeOpenTime ASC")
    List<Trade> findAllTradesWithinDate(final LocalDateTime start, final LocalDateTime end);

    /**
     * Returns a {@link Trade} for the given tradeId
     *
     * @param tradeId trade id
     * @return {@link Trade}
     */
    Trade findTradeByTradeId(final String tradeId);

    /**
     * Returns a {@link List} of {@link Trade}s that have not been processed
     *
     * @param processed processed flag
     * @return {@link List} of {@link Trade}
     */
    List<Trade> findAllByProcessed(final Boolean processed);
}
