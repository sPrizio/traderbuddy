package com.stephenprizio.traderbuddy.exceptions.importing;

/**
 * Exception for file extensions that are not supported by the system
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
public class FileExtensionNotSupportedException extends RuntimeException {

    public FileExtensionNotSupportedException(String message) {
        super(message);
    }
}
