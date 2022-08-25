package com.stephenprizio.traderbuddy.exceptions.importing;

/**
 * Exception for failures during the trade import process
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
public class TradeImportFailureException extends RuntimeException {

    public TradeImportFailureException(String message) {
        super(message);
    }

    public TradeImportFailureException(Throwable cause) {
        super(cause);
    }

    public TradeImportFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
