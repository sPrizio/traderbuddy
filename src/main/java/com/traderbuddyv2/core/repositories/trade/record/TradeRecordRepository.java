package com.traderbuddyv2.core.repositories.trade.record;

import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository level for {@link TradeRecord}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface TradeRecordRepository extends PagingAndSortingRepository<TradeRecord, Long> {

    /**
     * Finds the {@link TradeRecord} for the given start date, end date & interval
     *
     * @param startDate {@link LocalDate}
     * @param endDate {@link LocalDate}
     * @param aggregateInterval {@link AggregateInterval}
     * @param account {@link Account}
     * @return {@link TradeRecord}
     */
    TradeRecord findTradeRecordByStartDateAndEndDateAndAggregateIntervalAndAccount(final LocalDate startDate, final LocalDate endDate, final AggregateInterval aggregateInterval, final Account account);

    /**
     * Obtains a {@link List} of {@link TradeRecord}s ordered by their end dates in descending order for the given {@link AggregateInterval}
     *
     * @param count query limit
     * @param aggregateInterval {@link AggregateInterval}
     * @param accountId id of an {@link Account}
     * @return {@link List} of {@link TradeRecord}
     */
    @Query(nativeQuery = true, value = "SELECT * FROM trader_buddy.trade_records AS r WHERE r.aggregate_interval = :aggregateInterval AND r.account_id = :accountId ORDER BY r.end_date DESC LIMIT :count")
    List<TradeRecord> findRecentHistory(final @Param("count") int count, final @Param("aggregateInterval") Integer aggregateInterval, final @Param("accountId") Long accountId);

    /**
     * Obtains a {@link List} of {@link TradeRecord}s ordered by their start dates in ascending order that are within the given time span
     *
     * @param startDate {@link LocalDate}
     * @param endDate {@link LocalDate}
     * @param aggregateInterval {@link AggregateInterval}
     * @param account {@link Account}
     * @return {@link List} of {@link TradeRecord}
     */
    @Query("SELECT r FROM TradeRecord r WHERE r.startDate >= ?1 AND r.endDate < ?2 AND r.aggregateInterval = ?3 AND r.account = ?4 ORDER BY r.startDate ASC")
    List<TradeRecord> findHistory(final LocalDate startDate, final LocalDate endDate, final AggregateInterval aggregateInterval, final Account account);
}

