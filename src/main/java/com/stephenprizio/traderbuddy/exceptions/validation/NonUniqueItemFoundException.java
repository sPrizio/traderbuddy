package com.stephenprizio.traderbuddy.exceptions.validation;

/**
 * Custom exception for non unique items in the database
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class NonUniqueItemFoundException extends RuntimeException {

    public NonUniqueItemFoundException(String message) {
        super(message);
    }

    public NonUniqueItemFoundException(Throwable cause) {
        super(cause);
    }

    public NonUniqueItemFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
