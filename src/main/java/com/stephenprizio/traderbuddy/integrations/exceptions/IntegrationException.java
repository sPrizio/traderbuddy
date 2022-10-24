package com.stephenprizio.traderbuddy.integrations.exceptions;

/**
 * Custom exception for issues during external RESTful calls
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class IntegrationException extends RuntimeException {

    public IntegrationException(String message) {
        super(message);
    }

    public IntegrationException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
