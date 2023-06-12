package com.traderbuddyv2.core.constants;

import com.traderbuddyv2.core.models.entities.levelling.skill.Skill;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;

/**
 * Constants used for the core package
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class CoreConstants {

    /**
     * Generic message used when displaying an exception thrown from a class that should not have been instantiated
     */
    public static final String NO_INSTANTIATION = "%s classes should not be instantiated";

    private CoreConstants() {
        throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
    }

    /**
     * Represents a value that when encountered will basically act as a non-factor when returning a limited collection of entries. This value is akin
     * to asking the collection to not have a size limit, i.e. show me all results
     */
    public static final int MAX_RESULT_SIZE = -1;

    /**
     * Represents the lowest supported date in the system
     */
    public static final LocalDate MIN_DATE = LocalDate.of(1970, 1, 1);

    /**
     * Represents the highest supported date in the system
     */
    public static final LocalDate MAX_DATE = LocalDate.of(2201, 1, 1);

    /**
     * Represents the maximum allowable calendar year value
     */
    public static final int MAX_CALENDAR_YEAR = Year.MAX_VALUE;

    /**
     * The global TraderBuddy date format
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * The global TraderBuddy short time format
     */
    public static final String SHORT_TIME_FORMAT = "HH:mm";

    /**
     * The global TraderBuddy time format
     */
    public static final String TIME_FORMAT = "HH:mm:ss";

    /**
     * The global TraderBuddy date & time format
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    /**
     * All {@link Skill}s will start with a default step increment of 100
     */
    public static final int DEFAULT_SKILL_STEP_INCREMENT = 100;

    /**
     * All supported audio formats by the system
     */
    public static final List<String> SUPPORTED_AUDIO_FORMATS = List.of("mp3", "aac", "wav");

    /**
     * Global Phone number regex
     * (1) = country, (2) = area code, (3) = exchange, (4) = subscriber, (5) = extension where (x) indicates matcher group
     */
    public static final String PHONE_NUMBER_REGEX = "^\\s*(?:\\+?(\\d{1,3}))?[-. (]*(\\d{3})[-. )]*(\\d{3})[-. ]*(\\d{4})(?: *x(\\d+))?\\s*$";

    public static final String EASTERN_TIMEZONE = "America/Toronto";

    public static final String METATRADER4_TIMEZONE = "EET";

    public static class Validation {

        private Validation() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static final String ACCOUNT_CANNOT_BE_NULL = "account cannot be null";

        public static final String INTERVAL_CANNOT_BE_NULL = "interval cannot be null";

        public static final String START_DATE_CANNOT_BE_NULL = "start date cannot be null";

        public static final String END_DATE_CANNOT_BE_NULL = "end date cannot be null";

        public static final String DATE_CANNOT_BE_NULL = "date cannot be null";

        public static final String TRADE_RECORD_CANNOT_BE_NULL = "trade record cannot be null";

        public static final String UID_CANNOT_BE_NULL = "uid cannot be null";

        public static final String START_DATE_INVALID_FORMAT = "The start date %s was not of the expected format %s";

        public static final String END_DATE_INVALID_FORMAT = "The end date %s was not of the expected format %s";

        public static final String INVALID_INTERVAL = "%s was not a valid interval";

        public static final String MUTUALLY_EXCLUSIVE_DATES = "start date was after end date or vice versa";
    }
}
