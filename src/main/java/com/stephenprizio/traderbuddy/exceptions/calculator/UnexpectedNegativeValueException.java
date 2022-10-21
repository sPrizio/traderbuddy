package com.stephenprizio.traderbuddy.exceptions.calculator;

/**
 * Exception thrown when a negative number appears in an invalid location
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class UnexpectedNegativeValueException extends RuntimeException {

    public UnexpectedNegativeValueException(final String message) {
        super(message);
    }
}
