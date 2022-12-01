package com.traderbuddyv2.core.exceptions.validation;

/**
 * Custom exception for non unique items in the database
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class NonUniqueItemFoundException extends RuntimeException {

    public NonUniqueItemFoundException(final String message) {
        super(message);
    }

    public NonUniqueItemFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
