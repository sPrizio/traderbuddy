package com.traderbuddyv2.core.models.records.search;

import lombok.Getter;
import lombok.NonNull;

/**
 * Class representation of a generic search result
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record SearchResult(@Getter String query, @Getter String value, @Getter double score) implements Comparable<SearchResult> {


    //  METHODS

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SearchResult that = (SearchResult) o;
        return this.value.equals(that.value);
    }

    @Override
    public int compareTo(final @NonNull SearchResult o) {
        return Double.compare(this.score, o.score);
    }
}
