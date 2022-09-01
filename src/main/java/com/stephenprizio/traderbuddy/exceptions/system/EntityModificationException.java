package com.stephenprizio.traderbuddy.exceptions.system;

/**
 * Exception thrown when an entity cannot be modified correctly
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class EntityModificationException extends RuntimeException {

    public EntityModificationException(String message) {
        super(message);
    }
}
