package com.stephenprizio.traderbuddy.exceptions.validation;

/**
 * Custom exception for illegal method parameters
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class IllegalParameterException extends RuntimeException {

    public IllegalParameterException(String message) {
        super(message);
    }

    public IllegalParameterException(Throwable cause) {
        super(cause);
    }

    public IllegalParameterException(String message, Throwable cause) {
        super(message, cause);
    }
}
