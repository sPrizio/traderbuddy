package com.stephenprizio.traderbuddy.exceptions.validation;

/**
 * Exception for a missing json property
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class JsonMissingPropertyException extends RuntimeException {

    public JsonMissingPropertyException(String message) {
        super(message);
    }
}
