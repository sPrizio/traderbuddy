package com.stephenprizio.traderbuddy.exceptions;

/**
 * Custom exception for empty result sets
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class NoResultFoundException extends RuntimeException {

    public NoResultFoundException(String message) {
        super(message);
    }

    public NoResultFoundException(Throwable cause) {
        super(cause);
    }

    public NoResultFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
