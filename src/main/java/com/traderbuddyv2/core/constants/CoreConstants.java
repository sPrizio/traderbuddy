package com.traderbuddyv2.core.constants;

import java.time.Year;

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
     * Represents the maximum allowable calendar year value
     */
    public static final int MAX_CALENDAR_YEAR = Year.MAX_VALUE;

    /**
     * The global TraderBuddy date format
     */
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    /**
     * The global TraderBuddy date & time format
     */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";

    public static class Validation {

        private Validation() {
            throw new UnsupportedOperationException(String.format(NO_INSTANTIATION, getClass().getName()));
        }

        public static final String INTERVAL_CANNOT_BE_NULL = "interval cannot be null";

        public static final String START_DATE_CANNOT_BE_NULL = "start date cannot be null";

        public static final String END_DATE_CANNOT_BE_NULL = "end date cannot be null";

        public static final String UID_CANNOT_BE_NULL = "uid cannot be null";

        public static final String START_DATE_INVALID_FORMAT = "The start date %s was not of the expected format %s";

        public static final String END_DATE_INVALID_FORMAT = "The end date %s was not of the expected format %s";

        public static final String INVALID_INTERVAL = "%s was not a valid interval";

        public static final String MUTUALLY_EXCLUSIVE_DATES = "start date was after end date or vice versa";
    }
}
