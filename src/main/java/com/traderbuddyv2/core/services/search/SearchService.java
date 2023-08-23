package com.traderbuddyv2.core.services.search;

import com.traderbuddyv2.core.models.records.search.SearchResult;

import java.util.Set;

/**
 * Generic search service used throughout the system
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public interface SearchService {

    /**
     * Searches with the given query
     *
     * @param query search query
     * @return {@link Set} of {@link SearchResult}
     */
    Set<SearchResult> search(final String query);

    /**
     * Searches with the given query and limits the results for the given limit
     *
     * @param query search query
     * @param limit number of results to return
     * @return {@link Set} of {@link SearchResult}
     */
    Set<SearchResult> search(final String query, final int limit);
}
