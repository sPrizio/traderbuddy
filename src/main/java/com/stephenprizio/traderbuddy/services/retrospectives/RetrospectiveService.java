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
import com.stephenprizio.traderbuddy.services.platform.UniqueIdentifierService;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
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

    private static final String START_DATE_NOT_NULL = "start date cannot be null";
    private static final String END_DATE_NOT_NULL = "end date cannot be null";
    private static final String NULL_INTERVAL = "interval cannot be null";
    private static final String UID_NOT_NULL = "uid cannot be null";
    private static final String MUTUALLY_EXCLUSIVE_DATES = "startDate was after endDate or vice versa";

    @Resource(name = "retrospectiveEntryRepository")
    private RetrospectiveEntryRepository retrospectiveEntryRepository;

    @Resource(name = "retrospectiveRepository")
    private RetrospectiveRepository retrospectiveRepository;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


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

        validateParameterIsNotNull(start, START_DATE_NOT_NULL);
        validateParameterIsNotNull(end, END_DATE_NOT_NULL);
        validateParameterIsNotNull(interval, NULL_INTERVAL);
        validateDatesAreNotMutuallyExclusive(start.atStartOfDay(), end.atStartOfDay(), MUTUALLY_EXCLUSIVE_DATES);

        return Optional.ofNullable(this.retrospectiveRepository.findRetrospectiveByStartDateAndEndDateAndIntervalFrequency(start, end, interval));
    }

    /**
     * Returns a {@link Retrospective} for the given uid
     * @param uid uid
     * @return {@link Optional} {@link Retrospective}
     */
    public Optional<Retrospective> findRetrospectiveForUid(final String uid) {
        validateParameterIsNotNull(uid, UID_NOT_NULL);
        return this.retrospectiveRepository.findById(this.uniqueIdentifierService.retrieveIdForUid(uid));
    }

    /**
     * Returns a {@link List} of {@link LocalDate}s that represent the first day of a month that a user has a retrospective
     *
     * @return {@link List} of {@link LocalDate}
     */
    public List<LocalDate> findActiveRetrospectiveMonths() {
        Map<String, LocalDate> map = new TreeMap<>(String::compareTo);
        Iterable<Retrospective> retrospectives = this.retrospectiveRepository.findAll();
        retrospectives.forEach(retro -> map.put(retro.getStartDate().format(DateTimeFormatter.ofPattern("MMMM yyyy")), retro.getStartDate().with(TemporalAdjusters.firstDayOfMonth())));

        return new ArrayList<>(map.values());
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
     * @param uid uid
     * @param data {@link Map}
     * @return modified {@link Retrospective}
     */
    public Retrospective updateRetrospective(final String uid, final Map<String, Object> data) {

        validateParameterIsNotNull(uid, UID_NOT_NULL);

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for updating a Retrospective was null or empty");
        }

        try {
            Retrospective retrospective =
                    findRetrospectiveForUid(uid)
                            .orElseThrow(() -> new NoResultFoundException(String.format("No Retrospective found for uid %s", uid)));

            return applyChanges(retrospective, data);
        } catch (Exception e) {
            throw new EntityModificationException(String.format("An error occurred while modifying the Retrospective : %s", e.getMessage()));
        }
    }

    /**
     * Deletes the {@link Retrospective} for the given start date, end date and interval
     *
     * @param uid uid
     * @return {@link Optional} {@link Retrospective}
     */
    public Boolean deleteRetrospective(final String uid) {

        validateParameterIsNotNull(uid, UID_NOT_NULL);

        Optional<Retrospective> retrospective = findRetrospectiveForUid(uid);
        if (retrospective.isPresent()) {
            List<RetrospectiveEntry> oldEntries = retrospective.get().getPoints() != null ? new ArrayList<>(retrospective.get().getPoints()) : new ArrayList<>();
            for (RetrospectiveEntry e : oldEntries) {
                retrospective.get().removePoint(e);
                this.retrospectiveEntryRepository.delete(e);
            }

            this.retrospectiveRepository.delete(retrospective.get());
            return true;
        }

        return false;
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
