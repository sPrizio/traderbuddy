package com.traderbuddyv2.integration.exceptions;

/**
 * Custom exception for issues during external RESTful calls
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class IntegrationException extends RuntimeException {

    public IntegrationException(final String message) {
        super(message);
    }

    public IntegrationException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
