package com.traderbuddyv2.core.exceptions.system;

/**
 * Exception thrown when an entity cannot be modified correctly
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class EntityModificationException extends RuntimeException {

    public EntityModificationException(final String message) {
        super(message);
    }

    public EntityModificationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
