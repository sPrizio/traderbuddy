package com.stephenprizio.traderbuddy.repositories.retrospectives;

import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import com.stephenprizio.traderbuddy.models.entities.retrospectives.Retrospective;
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
     * Returns a {@link List} of {@link Retrospective}s that are within the given time span
     *
     * @param start {@link LocalDate} start of interval (inclusive)
     * @param end {@link LocalDate} end of interval (exclusive)
     * @return {@link List} of {@link Retrospective}s
     */
    @Query("SELECT r FROM Retrospective r WHERE r.startDate >= ?1 AND r.endDate < ?2 ORDER BY r.endDate DESC")
    List<Retrospective> findAllRetrospectivesWithinDate(final LocalDate start, final LocalDate end);

    /**
     * Returns a {@link Retrospective} for the given start, end dates and interval
     *
     * @param start {@link LocalDate}
     * @param end {@link LocalDate}
     * @param intervalFrequency {@link AggregateInterval}
     * @return {@link Retrospective}
     */
    Retrospective findRetrospectiveByStartDateAndEndDateAndIntervalFrequency(final LocalDate start, final LocalDate end, final AggregateInterval intervalFrequency);
}
