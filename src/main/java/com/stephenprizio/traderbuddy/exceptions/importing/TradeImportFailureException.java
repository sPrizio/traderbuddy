package com.stephenprizio.traderbuddy.exceptions.importing;

/**
 * Exception for failures during the trade import process
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradeImportFailureException extends RuntimeException {

    public TradeImportFailureException(String message) {
        super(message);
    }
}
