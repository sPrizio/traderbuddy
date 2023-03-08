package com.traderbuddyv2.core.services.retrospective;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.exceptions.system.EntityCreationException;
import com.traderbuddyv2.core.exceptions.system.EntityModificationException;
import com.traderbuddyv2.core.exceptions.validation.MissingRequiredDataException;
import com.traderbuddyv2.core.exceptions.validation.NoResultFoundException;
import com.traderbuddyv2.core.models.entities.retrospective.AudioRetrospective;
import com.traderbuddyv2.core.models.entities.retrospective.Retrospective;
import com.traderbuddyv2.core.models.entities.retrospective.RetrospectiveEntry;
import com.traderbuddyv2.core.repositories.retrospective.AudioRetrospectiveRepository;
import com.traderbuddyv2.core.repositories.retrospective.RetrospectiveEntryRepository;
import com.traderbuddyv2.core.repositories.retrospective.RetrospectiveRepository;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import com.traderbuddyv2.core.util.FileSystemUtils;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

import static com.traderbuddyv2.core.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;


/**
 * Service-layer for {@link Retrospective}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("retrospectiveService")
public class RetrospectiveService {

    @Resource(name = "audioRetrospectiveRepository")
    private AudioRetrospectiveRepository audioRetrospectiveRepository;

    @Resource(name = "retrospectiveEntryRepository")
    private RetrospectiveEntryRepository retrospectiveEntryRepository;

    @Resource(name = "retrospectiveRepository")
    private RetrospectiveRepository retrospectiveRepository;

    @Resource(name = "traderBuddyUserDetailsService")
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


    //  METHODS

    /**
     * Returns all {@link Retrospective}s within the given date span
     *
     * @param start {@link LocalDate} inclusive
     * @param end   {@link LocalDate} exclusive
     * @return {@link List} of {@link Retrospective}s
     */
    public List<Retrospective> findAllRetrospectivesWithinDate(final LocalDate start, final LocalDate end, final AggregateInterval interval) {

        validateParameterIsNotNull(start, CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(interval, CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start.atStartOfDay(), end.atStartOfDay(), CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);

        if (interval.equals(AggregateInterval.WEEKLY)) {
            return this.retrospectiveRepository.findAllRetrospectivesWithinDate(start.minusWeeks(1), end.plusWeeks(1), interval, this.traderBuddyUserDetailsService.getCurrentUser().getAccount());
        }

        return this.retrospectiveRepository.findAllRetrospectivesWithinDate(start, end, interval, this.traderBuddyUserDetailsService.getCurrentUser().getAccount());
    }

    /**
     * Returns all {@link AudioRetrospective}s within the given date span
     *
     * @param start {@link LocalDate} inclusive
     * @param end   {@link LocalDate} exclusive
     * @return {@link List} of {@link AudioRetrospective}s
     */
    public List<AudioRetrospective> findAllAudioRetrospectivesWithinDate(final LocalDate start, final LocalDate end, final AggregateInterval interval) {

        validateParameterIsNotNull(start, CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(interval, CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start.atStartOfDay(), end.atStartOfDay(), CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);

        if (interval.equals(AggregateInterval.WEEKLY)) {
            return this.audioRetrospectiveRepository.findAllRetrospectivesWithinDate(start.minusWeeks(1), end.plusWeeks(1), interval, this.traderBuddyUserDetailsService.getCurrentUser().getAccount());
        }

        return this.audioRetrospectiveRepository.findAllRetrospectivesWithinDate(start, end, interval, this.traderBuddyUserDetailsService.getCurrentUser().getAccount());
    }

    /**
     * Returns a {@link Retrospective} for the given start date, end date and interval
     *
     * @param start    {@link LocalDate}
     * @param end      {@link LocalDate}
     * @param interval {@link AggregateInterval}
     * @return {@link Optional} {@link Retrospective}
     */
    public Optional<Retrospective> findRetrospectiveForStartDateAndEndDateAndInterval(final LocalDate start, final LocalDate end, final AggregateInterval interval) {

        validateParameterIsNotNull(start, CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(interval, CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start.atStartOfDay(), end.atStartOfDay(), CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);

        return Optional.ofNullable(this.retrospectiveRepository.findRetrospectiveByStartDateAndEndDateAndIntervalFrequencyAndAccount(start, end, interval, this.traderBuddyUserDetailsService.getCurrentUser().getAccount()));
    }

    /**
     * Returns a {@link Retrospective} for the given uid
     *
     * @param uid uid
     * @return {@link Optional} {@link Retrospective}
     */
    public Optional<Retrospective> findRetrospectiveForUid(final String uid) {
        validateParameterIsNotNull(uid, CoreConstants.Validation.UID_CANNOT_BE_NULL);
        return this.retrospectiveRepository.findById(this.uniqueIdentifierService.retrieveIdForUid(uid));
    }

    /**
     * Returns a {@link List} of {@link LocalDate}s that represent the first day of a month that a user has a retrospective
     *
     * @return {@link List} of {@link LocalDate}
     */
    public List<LocalDate> findActiveRetrospectiveMonths(final int year) {

        Map<String, LocalDate> map = new HashMap<>();
        LocalDate compare = LocalDate.of(year, 1, 1);
        List<Retrospective> retrospectives = this.retrospectiveRepository.findAllRetrospectivesWithinDate(compare.minusWeeks(1), compare.plusYears(1), this.traderBuddyUserDetailsService.getCurrentUser().getAccount());
        retrospectives.forEach(retro -> {
            map.put(retro.getStartDate().format(DateTimeFormatter.ofPattern("MMMM yyyy")), retro.getStartDate().with(TemporalAdjusters.firstDayOfMonth()));
            map.put(retro.getEndDate().format(DateTimeFormatter.ofPattern("MMMM yyyy")), retro.getEndDate().with(TemporalAdjusters.firstDayOfMonth()));
        });

        return new ArrayList<>(map.values()).stream().filter(loc -> loc.getYear() == year).sorted(LocalDate::compareTo).sorted(Comparator.reverseOrder()).toList();
    }

    /**
     * Returns a {@link List} of {@link LocalDate}s that represent the first day of a year that a user has a retrospective
     *
     * @return {@link List} of {@link LocalDate}
     */
    public List<LocalDate> findActiveRetrospectiveYears() {
        Map<String, LocalDate> map = new HashMap<>();
        Iterable<Retrospective> retrospectives = this.retrospectiveRepository.findAllByAccount(this.traderBuddyUserDetailsService.getCurrentUser().getAccount());
        retrospectives.forEach(retro -> {
            map.put(retro.getStartDate().format(DateTimeFormatter.ofPattern("yyyy")), retro.getStartDate().with(TemporalAdjusters.firstDayOfYear()));
            map.put(retro.getEndDate().format(DateTimeFormatter.ofPattern("yyyy")), retro.getEndDate().with(TemporalAdjusters.firstDayOfYear()));
        });

        return new ArrayList<>(map.values()).stream().sorted(LocalDate::compareTo).sorted(Comparator.reverseOrder()).toList();
    }

    /**
     * Returns the most recent {@link Retrospective} for the given interval
     *
     * @param interval {@link AggregateInterval}
     * @return {@link Optional} {@link Retrospective}
     */
    public Optional<Retrospective> findMostRecentRetrospectiveForInterval(final AggregateInterval interval) {
        validateParameterIsNotNull(interval, CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);
        return Optional.ofNullable(this.retrospectiveRepository.findTopByIntervalFrequencyAndAccountOrderByStartDateDesc(interval, this.traderBuddyUserDetailsService.getCurrentUser().getAccount()));
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
            throw new EntityCreationException(String.format("A Retrospective could not be created : %s", e.getMessage()), e);
        }
    }

    /**
     * Updates an existing {@link Retrospective} with the given {@link Map} of data. Update methods are designed to be idempotent.
     *
     * @param uid  uid
     * @param data {@link Map}
     * @return modified {@link Retrospective}
     */
    public Retrospective updateRetrospective(final String uid, final Map<String, Object> data) {

        validateParameterIsNotNull(uid, CoreConstants.Validation.UID_CANNOT_BE_NULL);

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for updating a Retrospective was null or empty");
        }

        try {
            Retrospective retrospective =
                    findRetrospectiveForUid(uid)
                            .orElseThrow(() -> new NoResultFoundException(String.format("No Retrospective found for uid %s", uid)));

            return applyChanges(retrospective, data);
        } catch (Exception e) {
            throw new EntityModificationException(String.format("An error occurred while modifying the Retrospective : %s", e.getMessage()), e);
        }
    }

    /**
     * Deletes the {@link Retrospective} for the given uid
     *
     * @param uid uid
     * @return true if deleted, false if not
     */
    public boolean deleteRetrospective(final String uid) {

        validateParameterIsNotNull(uid, CoreConstants.Validation.UID_CANNOT_BE_NULL);

        Optional<Retrospective> retrospective = findRetrospectiveForUid(uid);
        if (retrospective.isPresent()) {
            List<RetrospectiveEntry> oldEntries = retrospective.get().getPoints() != null ? new ArrayList<>(retrospective.get().getPoints()) : new ArrayList<>();
            for (RetrospectiveEntry e : oldEntries) {
                retrospective.get().removePoint(e);
                this.retrospectiveEntryRepository.delete(e);
            }

            retrospective.get().setAccount(null);
            this.retrospectiveRepository.save(retrospective.get());
            this.retrospectiveRepository.delete(retrospective.get());

            return true;
        }

        return false;
    }

    /**
     * Saves an {@link AudioRetrospective} from the given {@link MultipartFile}
     *
     * @param start             start date
     * @param end               end date
     * @param aggregateInterval {@link AggregateInterval}
     * @param name              name of file
     * @param file              {@link MultipartFile}
     * @return empty message if true
     */
    public String saveAudio(final LocalDate start, final LocalDate end, final AggregateInterval aggregateInterval, final String name, final MultipartFile file) {

        final AudioRetrospective audioRetrospective = new AudioRetrospective();

        audioRetrospective.setStartDate(start);
        audioRetrospective.setEndDate(end);
        audioRetrospective.setIntervalFrequency(aggregateInterval);
        audioRetrospective.setAccount(this.traderBuddyUserDetailsService.getCurrentUser().getAccount());
        audioRetrospective.setName(name);
        audioRetrospective.setUrl("\\audio\\" + file.getOriginalFilename());

        try {
            file.transferTo(new File(FileSystemUtils.getContentRoot(true) + "\\audio\\" + file.getOriginalFilename()));
            this.audioRetrospectiveRepository.save(audioRetrospective);
        } catch (Exception e) {
            return e.getMessage();
        }

        return StringUtils.EMPTY;
    }


    //  HELPERS

    /**
     * Applies changes to the given {@link Retrospective} with the given data
     *
     * @param retrospective {@link Retrospective}
     * @param data          {@link Map}
     * @return updated {@link Retrospective}
     */
    private Retrospective applyChanges(Retrospective retrospective, final Map<String, Object> data) {

        Map<String, Object> retro = (Map<String, Object>) data.get("retrospective");

        retrospective.setStartDate(LocalDate.parse(retro.get("startDate").toString(), DateTimeFormatter.ISO_DATE));
        retrospective.setEndDate(LocalDate.parse(retro.get("endDate").toString(), DateTimeFormatter.ISO_DATE));
        retrospective.setIntervalFrequency(AggregateInterval.valueOf(retro.get("intervalFrequency").toString()));
        retrospective.setAccount(this.traderBuddyUserDetailsService.getCurrentUser().getAccount());

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
