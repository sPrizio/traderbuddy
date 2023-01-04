package com.traderbuddyv2.core.repositories.trade;

import com.traderbuddyv2.core.enums.trades.TradeType;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
     * @param account {@link Account}
     * @return {@link List} of {@link Trade}s
     */
    List<Trade> findAllByTradeTypeAndAccountOrderByTradeOpenTimeAsc(final TradeType tradeType, final Account account);

    /**
     * Returns a {@link List} of {@link Trade}s that are within the given time span
     *
     * @param start {@link LocalDateTime} start of interval (inclusive)
     * @param end {@link LocalDateTime} end of interval (exclusive)
     * @param account {@link Account}
     * @return {@link List} of {@link Trade}s
     */
    @Query("SELECT t FROM Trade t WHERE t.tradeOpenTime >= ?1 AND t.tradeCloseTime < ?2 AND t.account = ?3 AND t.processed = true ORDER BY t.tradeOpenTime ASC")
    List<Trade> findAllTradesWithinDate(final LocalDateTime start, final LocalDateTime end, final Account account);

    /**
     * Returns a paginated {@link List} of {@link Trade}s that are within the given time span
     *
     * @param start {@link LocalDateTime} start of interval (inclusive)
     * @param end {@link LocalDateTime} end of interval (exclusive)
     * @param account {@link Account}
     * @param pageable {@link Pageable}
     * @return {@link List} of {@link Trade}s
     */
    @Query("SELECT t FROM Trade t WHERE t.tradeOpenTime >= ?1 AND t.tradeCloseTime < ?2 AND t.account = ?3 AND t.processed = true ORDER BY t.tradeOpenTime ASC")
    Page<Trade> findAllTradesWithinDate(final LocalDateTime start, final LocalDateTime end, final Account account, final Pageable pageable);

    /**
     * Returns a paginated {@link List} of {@link Trade}s that are within the given time span
     *
     * @param start {@link LocalDateTime} start of interval (inclusive)
     * @param end {@link LocalDateTime} end of interval (exclusive)
     * @param account {@link Account}
     * @param pageable {@link Pageable}
     * @return {@link List} of {@link Trade}s
     */
    @Query("SELECT t FROM Trade t WHERE t.tradeOpenTime >= ?1 AND t.tradeCloseTime < ?2 AND t.account = ?3 AND t.relevant = true AND t.processed = true ORDER BY t.tradeOpenTime ASC")
    Page<Trade> findAllRelevantTradesWithinDate(final LocalDateTime start, final LocalDateTime end, final Account account, final Pageable pageable);

    /**
     * Returns a {@link Trade} for the given tradeId
     *
     * @param tradeId trade id
     * @param account {@link Account
     * @return {@link Trade}
     */
    Trade findTradeByTradeIdAndAccount(final String tradeId, final Account account);

    /**
     * Returns a {@link List} of {@link Trade}s that have not been processed
     *
     * @param processed processed flag
     * @param account {@link Account
     * @return {@link List} of {@link Trade}
     */
    List<Trade> findAllByProcessedAndAccountAndRelevantIsTrueOrderByTradeOpenTimeAsc(final boolean processed, final Account account);

    /**
     * Returns a {@link List} of {@link Trade} for the given account
     *
     * @param account {@link Account}
     * @return {@link List} of {@link Trade}
     */
    List<Trade> findAllByAccount(final Account account);

    /**
     * Returns a {@link List} of {@link Trade}s that are within the given time span
     *
     * @param start {@link LocalDateTime} start of interval (inclusive)
     * @param end {@link LocalDateTime} end of interval (exclusive)
     * @param account {@link Account}
     * @return {@link List} of {@link Trade}s
     */
    @Query("SELECT t FROM Trade t WHERE t.tradeOpenTime >= ?1 AND t.tradeCloseTime < ?2 AND t.account = ?3 AND t.relevant = true ORDER BY t.tradeOpenTime ASC")
    List<Trade> findAllTradesForTradeRecord(final LocalDateTime start, final LocalDateTime end, final Account account);
}

