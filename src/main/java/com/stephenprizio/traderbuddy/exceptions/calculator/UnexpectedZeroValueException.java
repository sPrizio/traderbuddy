package com.stephenprizio.traderbuddy.exceptions.calculator;

/**
 * Exception thrown when a zero value appears in an invalid location
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
public class UnexpectedZeroValueException extends RuntimeException {

    public UnexpectedZeroValueException(String message) {
        super(message);
    }
}
