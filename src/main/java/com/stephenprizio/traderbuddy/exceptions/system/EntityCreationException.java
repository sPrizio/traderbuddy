package com.stephenprizio.traderbuddy.exceptions.system;

/**
 * Exception thrown when an entity cannot be created properly
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class EntityCreationException extends RuntimeException {

    public EntityCreationException(String message) {
        super(message);
    }
}
