package com.stephenprizio.traderbuddy.exceptions.system;

/**
 * A generic system exception that can be thrown from anywhere
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class GenericSystemException extends RuntimeException {

    public GenericSystemException(String message) {
        super(message);
    }
}
