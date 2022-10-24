package com.stephenprizio.traderbuddy.constants;

import java.time.Year;

/**
 * Global constants used throughout the TraderBuddy App
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TraderBuddyConstants {

    /**
     * Generic message used when displaying an exception thrown from a class that should not have been instantiated
     */
    public static final String NO_INSTANTIATION = "%s classes should not be instantiated";

    private TraderBuddyConstants() {
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
     * Generic error message for client errors
     */
    public static final String CLIENT_ERROR_DEFAULT_MESSAGE = "Looks like your request could not be processed. Check your inputs and try again!";

    /**
     * Generic error message for server errors
     */
    public static final String SERVER_ERROR_DEFAULT_MESSAGE = "An error on our side occurred. Please try again.";
}
