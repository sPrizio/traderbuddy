package com.traderbuddyv2.core.exceptions.system;

/**
 * Exception thrown when an anonymous user tries to operate in the app
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class AnonymousUserException extends RuntimeException {

    public AnonymousUserException(final String message) {
        super(message);
    }

    public AnonymousUserException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
