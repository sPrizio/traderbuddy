package com.traderbuddyv2.core.repositories.news;

import com.traderbuddyv2.core.models.entities.news.MarketNewsSlot;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link MarketNewsSlot}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface MarketNewsSlotRepository extends PagingAndSortingRepository<MarketNewsSlot, Long> {
}
