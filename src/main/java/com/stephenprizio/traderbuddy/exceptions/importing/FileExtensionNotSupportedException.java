package com.stephenprizio.traderbuddy.exceptions.importing;

/**
 * Exception for file extensions that are not supported by the system
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class FileExtensionNotSupportedException extends RuntimeException {

    public FileExtensionNotSupportedException(final String message) {
        super(message);
    }
}
