package com.stephenprizio.traderbuddy.repositories.records;

import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import com.stephenprizio.traderbuddy.models.entities.records.TradeRecord;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

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
     * @return {@link TradeRecord}
     */
    TradeRecord findTradingRecordByStartDateAndEndDateAndAggregateInterval(final LocalDate startDate, final LocalDate endDate, final AggregateInterval aggregateInterval);
}
