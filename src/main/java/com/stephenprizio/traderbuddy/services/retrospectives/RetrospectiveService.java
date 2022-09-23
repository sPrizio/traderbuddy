package com.stephenprizio.traderbuddy.services.retrospectives;

import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import com.stephenprizio.traderbuddy.exceptions.system.EntityCreationException;
import com.stephenprizio.traderbuddy.exceptions.system.EntityModificationException;
import com.stephenprizio.traderbuddy.exceptions.validation.MissingRequiredDataException;
import com.stephenprizio.traderbuddy.exceptions.validation.NoResultFoundException;
import com.stephenprizio.traderbuddy.models.entities.retrospectives.Retrospective;
import com.stephenprizio.traderbuddy.models.entities.retrospectives.RetrospectiveEntry;
import com.stephenprizio.traderbuddy.repositories.retrospectives.RetrospectiveEntryRepository;
import com.stephenprizio.traderbuddy.repositories.retrospectives.RetrospectiveRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

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

    private static final String NULL_INTERVAL = "interval cannot be null";
    private static final String MUTUALLY_EXCLUSIVE_DATES = "startDate was after endDate or vice versa";

    @Resource(name = "retrospectiveEntryRepository")
    private RetrospectiveEntryRepository retrospectiveEntryRepository;

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
    public List<Retrospective> findAllRetrospectivesWithinDate(final LocalDate start, final LocalDate end, final AggregateInterval interval) {

        validateParameterIsNotNull(start, "startDate cannot be null");
        validateParameterIsNotNull(end, "endDate cannot be null");
        validateParameterIsNotNull(interval, NULL_INTERVAL);
        validateDatesAreNotMutuallyExclusive(start.atStartOfDay(), end.atStartOfDay(), MUTUALLY_EXCLUSIVE_DATES);

        return this.retrospectiveRepository.findAllRetrospectivesWithinDate(start, end, interval);
    }

    /**
     * Returns a {@link Retrospective} for the given start date, end date and interval
     *
     * @param start {@link LocalDate}
     * @param end {@link LocalDate}
     * @param interval {@link AggregateInterval}
     * @return {@link Optional} {@link Retrospective}
     */
    public Optional<Retrospective> findRetrospectiveForStartDateAndEndDateAndInterval(final LocalDate start, final LocalDate end, final AggregateInterval interval) {

        validateParameterIsNotNull(start, "start date cannot be null");
        validateParameterIsNotNull(end, "end date cannot be null");
        validateParameterIsNotNull(interval, NULL_INTERVAL);
        validateDatesAreNotMutuallyExclusive(start.atStartOfDay(), end.atStartOfDay(), MUTUALLY_EXCLUSIVE_DATES);

        return Optional.ofNullable(this.retrospectiveRepository.findRetrospectiveByStartDateAndEndDateAndIntervalFrequency(start, end, interval));
    }

    /**
     * Creates a new {@link Retrospective} from the given {@link Map} of data
     *
     * @param data {@link Map}
     * @return newly created {@link Retrospective}
     */
    public Retrospective createRetrospective(final Map<String, Object> data) {

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for creating a Retrospective was null or empty");
        }

        try {
            return applyChanges(new Retrospective(), data);
        } catch (Exception e) {
            throw new EntityCreationException(String.format("A Retrospective could not be created : %s", e.getMessage()));
        }
    }

    /**
     * Updates an existing {@link Retrospective} with the given {@link Map} of data. Update methods are designed to be idempotent.
     *
     * @param start {@link LocalDate}
     * @param end {@link LocalDate}
     * @param interval {@link AggregateInterval}
     * @param data {@link Map}
     * @return modified {@link Retrospective}
     */
    public Retrospective updateRetrospective(final LocalDate start, final LocalDate end, final AggregateInterval interval, final Map<String, Object> data) {

        validateParameterIsNotNull(start, "start date cannot be null");
        validateParameterIsNotNull(end, "end date cannot be null");
        validateParameterIsNotNull(interval, NULL_INTERVAL);
        validateDatesAreNotMutuallyExclusive(start.atStartOfDay(), end.atStartOfDay(), MUTUALLY_EXCLUSIVE_DATES);

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for updating a Retrospective was null or empty");
        }

        try {
            Retrospective retrospective =
                    findRetrospectiveForStartDateAndEndDateAndInterval(start, end, interval)
                            .orElseThrow(() -> new NoResultFoundException(String.format("No Retrospective found for start date %s, end date %s and interval %s", start.format(DateTimeFormatter.ISO_DATE), end.format(DateTimeFormatter.ISO_DATE), interval.name())));

            return applyChanges(retrospective, data);
        } catch (Exception e) {
            throw new EntityModificationException(String.format("An error occurred while modifying the Retrospective : %s", e.getMessage()));
        }
    }


    //  HELPERS

    /**
     * Applies changes to the given {@link Retrospective} with the given data
     *
     * @param retrospective {@link Retrospective}
     * @param data {@link Map}
     * @return updated {@link Retrospective}
     */
    private Retrospective applyChanges(Retrospective retrospective, final Map<String, Object> data) {

        Map<String, Object> retro = (Map<String, Object>) data.get("retrospective");

        retrospective.setStartDate(LocalDate.parse(retro.get("startDate").toString(), DateTimeFormatter.ISO_DATE));
        retrospective.setEndDate(LocalDate.parse(retro.get("endDate").toString(), DateTimeFormatter.ISO_DATE));
        retrospective.setIntervalFrequency(AggregateInterval.valueOf(retro.get("intervalFrequency").toString()));

        retrospective = this.retrospectiveRepository.save(retrospective);

        if (retro.containsKey("points")) {
            List<RetrospectiveEntry> oldEntries = retrospective.getPoints() != null ? new ArrayList<>(retrospective.getPoints()) : new ArrayList<>();
            for (RetrospectiveEntry e : oldEntries) {
                retrospective.removePoint(e);
                this.retrospectiveEntryRepository.delete(e);
            }

            retrospective = this.retrospectiveRepository.save(retrospective);

            List<RetrospectiveEntry> entries = new ArrayList<>();
            List<Map<String, Object>> points = (List<Map<String, Object>>) retro.get("points");

            for (Map<String, Object> point : points) {
                RetrospectiveEntry entry = new RetrospectiveEntry();
                entry.setEntryText(point.get("entryText").toString());
                entry.setKeyPoint(Boolean.parseBoolean(point.get("keyPoint").toString()));
                entry.setLineNumber(Integer.parseInt(point.get("lineNumber").toString()));

                entry = this.retrospectiveEntryRepository.save(entry);
                retrospective.addPoint(entry);
                entries.add(entry);
            }
        }

        return this.retrospectiveRepository.save(retrospective);
    }
}
