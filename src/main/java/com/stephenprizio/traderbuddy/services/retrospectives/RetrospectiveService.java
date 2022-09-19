package com.stephenprizio.traderbuddy.services.retrospectives;

import com.stephenprizio.traderbuddy.models.entities.retrospectives.Retrospective;
import com.stephenprizio.traderbuddy.repositories.retrospectives.RetrospectiveRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.List;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link Retrospective}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("retrospectiveService")
public class RetrospectiveService {

    @Resource(name = "retrospectiveRepository")
    private RetrospectiveRepository retrospectiveRepository;


    //  METHODS

    /**
     * Returns all {@link Retrospective}s within the given date span
     *
     * @param start {@link LocalDate} inclusive
     * @param end {@link LocalDate} exclusive
     * @return {@link List} of {@link Retrospective}s
     */
    public List<Retrospective> findAllRetrospectivesWithinDate(final LocalDate start, final LocalDate end) {

        validateParameterIsNotNull(start, "startDate cannot be null");
        validateParameterIsNotNull(end, "endDate cannot be null");
        validateDatesAreNotMutuallyExclusive(start.atStartOfDay(), end.atStartOfDay(), "startDate was after endDate or vice versa");

        return this.retrospectiveRepository.findAllRetrospectivesWithinDate(start, end);
    }
}
