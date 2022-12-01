package com.traderbuddyv2.importing;

import java.io.InputStream;

/**
 * Defines the import service architecture for importing trades into the system
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public interface ImportService {

    /**
     * Imports trades from a CSV file using a file path
     *
     * @param filePath file path
     * @param delimiter delimiter
     */
    void importTrades(final String filePath, final Character delimiter);

    /**
     * Imports trades from a CSV file using an {@link InputStream}
     *
     * @param inputStream {@link InputStream}
     * @param delimiter unit delimiter
     */
    void importTrades(final InputStream inputStream, final Character delimiter);
}
