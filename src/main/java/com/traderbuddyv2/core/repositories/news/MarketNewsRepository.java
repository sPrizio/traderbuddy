package com.traderbuddyv2.core.repositories.news;

import com.traderbuddyv2.core.models.entities.news.MarketNews;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Data-access layer for {@link MarketNews}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface MarketNewsRepository extends PagingAndSortingRepository<MarketNews, Long> {

    /**
     * Returns a {@link List} of {@link MarketNews} within the given start & end dates
     *
     * @param start {@link LocalDate} start
     * @param end {@link LocalDate} ned
     * @return {@link List} of {@link MarketNews}
     */
    @Query("SELECT n FROM MarketNews n WHERE n.date >= ?1 AND n.date < ?2 ORDER BY n.date ASC")
    List<MarketNews> findNewsWithinInterval(final LocalDate start, final LocalDate end);
}
