package com.traderbuddyv2.core.exceptions.system;

/**
 * Exception thrown when search fails
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class SearchException extends RuntimeException {

    public SearchException(final String message) {
        super(message);
    }

    public SearchException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
