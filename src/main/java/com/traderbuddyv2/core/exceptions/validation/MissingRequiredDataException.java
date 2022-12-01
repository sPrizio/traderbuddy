package com.traderbuddyv2.core.exceptions.validation;

/**
 * Exception thrown when required data is not available or is missing
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class MissingRequiredDataException extends RuntimeException {

    public MissingRequiredDataException(String message) {
        super(message);
    }

    public MissingRequiredDataException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
