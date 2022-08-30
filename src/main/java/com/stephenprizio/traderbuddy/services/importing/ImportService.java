package com.stephenprizio.traderbuddy.services.importing;

/**
 * Defines the import service architecture for importing trades into the system
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public interface ImportService {

    /**
     * Imports trades from a CSV file from the CMC platform
     *
     * @param filePath file path
     * @param delimiter delimiter
     */
    void importTrades(final String filePath, final String delimiter);
}
