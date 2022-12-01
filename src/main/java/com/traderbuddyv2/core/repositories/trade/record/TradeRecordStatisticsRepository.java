package com.traderbuddyv2.core.repositories.trade.record;

import com.traderbuddyv2.core.models.entities.trade.record.TradeRecordStatistics;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link TradeRecordStatistics}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface TradeRecordStatisticsRepository extends PagingAndSortingRepository<TradeRecordStatistics, Long> {
}
