package com.traderbuddyv2.core.exceptions.validation;

/**
 * Exception for a missing json property
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class JsonMissingPropertyException extends RuntimeException {

    public JsonMissingPropertyException(final String message) {
        super(message);
    }

    public JsonMissingPropertyException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
