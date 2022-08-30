package com.stephenprizio.traderbuddy.exceptions.validation;

/**
 * Exception for a missing json propetry
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
public class JsonMissingPropertyException extends RuntimeException {

    public JsonMissingPropertyException(String message) {
        super(message);
    }
}
