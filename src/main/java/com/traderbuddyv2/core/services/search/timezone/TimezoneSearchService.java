package com.traderbuddyv2.core.services.search.timezone;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.exceptions.system.SearchException;
import com.traderbuddyv2.core.models.records.search.SearchResult;
import com.traderbuddyv2.core.services.search.SearchService;
import org.apache.commons.text.similarity.FuzzyScore;
import org.apache.commons.text.similarity.JaroWinklerDistance;
import org.apache.commons.text.similarity.JaroWinklerSimilarity;
import org.apache.commons.text.similarity.LevenshteinDistance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service that searches for timezones that match the given inputs
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("timezoneSearchService")
public class TimezoneSearchService implements SearchService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimezoneSearchService.class);
    private static final List<String> EXCLUDED_WORDS =
            List.of(
                    "Africa",
                    "America",
                    "Antarctica",
                    "Arctic",
                    "Asia",
                    "Atlantic",
                    "Australia",
                    "Brazil",
                    "Canada",
                    "Etc",
                    "Europe",
                    "Indian",
                    "Mexico",
                    "Pacific",
                    "US"
            );


    //  METHODS

    @Override
    public Set<SearchResult> search(final String query) {
        validateParameterIsNotNull(query, CoreConstants.Validation.SEARCH_QUERY_CANNOT_BE_NULL);
        try (final BufferedReader bufferedReader = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:timezones/list.txt")))) {
            final List<SearchResult> results = getSearch(bufferedReader, query);
            return new LinkedHashSet<>(results);
        } catch (Exception e) {
            LOGGER.error("The import process failed with reason : {}", e.getMessage(), e);
            throw new SearchException(String.format("Search failed with reason : %s", e.getMessage()), e);
        }
    }

    @Override
    public Set<SearchResult> search(final String query, final int limit) {
        validateParameterIsNotNull(query, CoreConstants.Validation.SEARCH_QUERY_CANNOT_BE_NULL);
        try (final BufferedReader bufferedReader = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:timezones/list.txt")))) {
            final List<SearchResult> results = getSearch(bufferedReader, query).subList(0, limit);
            return new LinkedHashSet<>(results);
        } catch (Exception e) {
            LOGGER.error("The import process failed with reason : {}", e.getMessage(), e);
            throw new SearchException(String.format("Search failed with reason : %s", e.getMessage()), e);
        }
    }


    //  HELPERS

    /**
     * Obtains the search results
     *
     * @param bufferedReader input data
     * @param query search query
     * @return {@link List} of {@link SearchResult}
     */
    private List<SearchResult> getSearch(final BufferedReader bufferedReader, final String query) {
        return
                bufferedReader
                        .lines()
                        /*.map(line -> new SearchResult(query, line, fuzzySearch(query, sanitizeQuery(line))))*/
                        .map(line -> new SearchResult(query, line, jaroWinklerDistance(query, sanitizeQuery(line))))
                        /*.map(line -> new SearchResult(query, line, jaroWinklerSimilarity(query, sanitizeQuery(line))))*/
                        /*.map(line -> new SearchResult(query, line, levenshteinDistance(query, sanitizeQuery(line))))*/
                        .sorted(Comparator.comparing(SearchResult::getScore))
                        .toList();

    }

    /**
     * Sanitizes a string for search ranking
     *
     * @param query input
     * @return sanitized string
     */
    private String sanitizeQuery(final String query) {

        String cleanQuery = query;
        /*for (final String s : EXCLUDED_WORDS) {
            if (cleanQuery.contains(s)) {
                cleanQuery = cleanQuery.replace(s, StringUtils.EMPTY);
            }
        }*/

        return cleanQuery
                .toLowerCase()
                .replace("/", " ")
                .replace("_", " ");
    }

    /**
     * Basic implementation of a fuzzy search as per this <a href="https://commons.apache.org/proper/commons-text/javadocs/api-release/org/apache/commons/text/similarity/FuzzyScore.html">Javadoc</a>
     * A higher score indicates more of a match
     *
     * @param term  term
     * @param query search query
     * @return score
     */
    private double fuzzySearch(final String term, final String query) {
        return new FuzzyScore(Locale.ENGLISH).fuzzyScore(term, query);
    }

    /**
     * Basic implementation of the Jaro Winkler Distance as per this <a href="https://commons.apache.org/proper/commons-text/javadocs/api-release/org/apache/commons/text/similarity/JaroWinklerDistance.html">Javadoc</a>
     * A lower score indicates more of a match
     *
     * @param term  term
     * @param query search query
     * @return score
     */
    private double jaroWinklerDistance(final String term, final String query) {
        return new JaroWinklerDistance().apply(term, query);
    }

    /**
     * Basic implementation of the Jaro Winkler Similarity as per this <a href="https://commons.apache.org/proper/commons-text/javadocs/api-release/org/apache/commons/text/similarity/JaroWinklerSimilarity.html">Javadoc</a>
     * A higher score indicates more of a match
     *
     * @param term  term
     * @param query search query
     * @return score
     */
    private double jaroWinklerSimilarity(final String term, final String query) {
        return new JaroWinklerSimilarity().apply(term, query);
    }

    /**
     * Basic implementation of the Levenshtein Distance as per this <a href="https://commons.apache.org/proper/commons-text/javadocs/api-release/org/apache/commons/text/similarity/LevenshteinDistance.html">Javadoc</a>
     * A lower score indicates more of a match
     *
     * @param term  term
     * @param query search query
     * @return score
     */
    private double levenshteinDistance(final String term, final String query) {
        return new LevenshteinDistance().apply(term, query);
    }
}
