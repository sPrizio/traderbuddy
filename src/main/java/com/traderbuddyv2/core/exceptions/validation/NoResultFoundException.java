package com.traderbuddyv2.core.exceptions.validation;

/**
 * Custom exception for empty result sets
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class NoResultFoundException extends RuntimeException {

    public NoResultFoundException(final String message) {
        super(message);
    }

    public NoResultFoundException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
