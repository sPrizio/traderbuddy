package com.traderbuddyv2.core.exceptions.system;

/**
 * Exception thrown when an entity cannot be created properly
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class EntityCreationException extends RuntimeException {

    public EntityCreationException(final String message) {
        super(message);
    }

    public EntityCreationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
