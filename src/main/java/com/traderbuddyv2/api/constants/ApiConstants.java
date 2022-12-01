package com.traderbuddyv2.api.constants;

import com.traderbuddyv2.core.constants.CoreConstants;

/**
 * Constants used for the Api package
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class ApiConstants {

    private ApiConstants() {
        throw new UnsupportedOperationException(String.format(CoreConstants.NO_INSTANTIATION, getClass().getName()));
    }

    /**
     * Generic error message for client errors
     */
    public static final String CLIENT_ERROR_DEFAULT_MESSAGE = "Looks like your request could not be processed. Check your inputs and try again!";

    /**
     * Generic error message for server errors
     */
    public static final String SERVER_ERROR_DEFAULT_MESSAGE = "An error on our side occurred. Please try again.";
}
