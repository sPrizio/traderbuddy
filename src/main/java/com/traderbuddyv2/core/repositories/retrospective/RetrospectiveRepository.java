package com.traderbuddyv2.core.repositories.retrospective;

import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.retrospective.Retrospective;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for {@link Retrospective}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface RetrospectiveRepository extends CrudRepository<Retrospective, Long> {

    /**
     * Returns a {@link List} of {@link Retrospective}s that are within the given time span for the given {@link AggregateInterval}
     *
     * @param start {@link LocalDate} start of interval (inclusive)
     * @param end {@link LocalDate} end of interval (exclusive)
     * @param interval {@link AggregateInterval}
     * @param account {@link Account}
     * @return {@link List} of {@link Retrospective}s
     */
    @Query("SELECT r FROM Retrospective r WHERE r.startDate >= ?1 AND r.endDate < ?2 AND r.intervalFrequency = ?3  AND r.account = ?4 ORDER BY r.endDate DESC")
    List<Retrospective> findAllRetrospectivesWithinDate(final LocalDate start, final LocalDate end, final AggregateInterval interval, final Account account);

    /**
     * Returns a {@link List} of {@link Retrospective}s that are within the given time span
     *
     * @param start {@link LocalDate} start of interval (inclusive)
     * @param end {@link LocalDate} end of interval (exclusive)
     * @param account {@link Account}
     * @return {@link List} of {@link Retrospective}s
     */
    @Query("SELECT r FROM Retrospective r WHERE r.startDate >= ?1 AND r.endDate < ?2 AND r.account = ?3 ORDER BY r.endDate DESC")
    List<Retrospective> findAllRetrospectivesWithinDate(final LocalDate start, final LocalDate end, final Account account);

    /**
     * Returns a {@link Retrospective} for the given start, end dates and interval
     *
     * @param start {@link LocalDate}
     * @param end {@link LocalDate}
     * @param intervalFrequency {@link AggregateInterval}
     * @param account {@link Account}
     * @return {@link Retrospective}
     */
    Retrospective findRetrospectiveByStartDateAndEndDateAndIntervalFrequencyAndAccount(final LocalDate start, final LocalDate end, final AggregateInterval intervalFrequency, final Account account);

    /**
     * Returns the most recent {@link Retrospective} for the given {@link AggregateInterval}
     *
     * @param intervalFrequency {@link AggregateInterval}
     * @param account {@link Account}
     * @return {@link Retrospective}
     */
    Retrospective findTopByIntervalFrequencyAndAccountOrderByStartDateDesc(final AggregateInterval intervalFrequency, final Account account);

    /**
     * Returns a {@link List} of {@link Retrospective}s for the given {@link Account}
     *
     * @param account {@link Account}
     * @return {@link List} of  {@link Retrospective}
     */
    List<Retrospective> findAllByAccount(final Account account);
}
