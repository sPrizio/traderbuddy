package com.traderbuddyv2.api.exceptions;

public class InvalidEnumException extends RuntimeException {

    public InvalidEnumException(final String message) {
        super(message);
    }

    public InvalidEnumException(final String message, final Throwable throwable) {
        super(message, throwable);
    }
}
