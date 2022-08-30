package com.stephenprizio.traderbuddy.exceptions.validation;

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
}
