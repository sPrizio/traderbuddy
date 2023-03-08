package com.traderbuddyv2.core.repositories.retrospective;

import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.retrospective.AudioRetrospective;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Data-access layer for {@link AudioRetrospective}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface AudioRetrospectiveRepository extends PagingAndSortingRepository<AudioRetrospective, Long> {

    /**
     * Returns a {@link List} of {@link AudioRetrospective}s that are within the given time span for the given {@link AggregateInterval}
     *
     * @param start {@link LocalDate} start of interval (inclusive)
     * @param end {@link LocalDate} end of interval (exclusive)
     * @param interval {@link AggregateInterval}
     * @param account {@link Account}
     * @return {@link List} of {@link AudioRetrospective}s
     */
    @Query("SELECT r FROM AudioRetrospective r WHERE r.startDate >= ?1 AND r.endDate < ?2 AND r.intervalFrequency = ?3  AND r.account = ?4 ORDER BY r.endDate DESC")
    List<AudioRetrospective> findAllRetrospectivesWithinDate(final LocalDate start, final LocalDate end, final AggregateInterval interval, final Account account);
}
