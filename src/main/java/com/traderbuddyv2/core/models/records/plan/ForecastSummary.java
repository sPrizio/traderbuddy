package com.traderbuddyv2.core.models.records.plan;

import lombok.Getter;

import java.util.List;

/**
 * Represents a summary of {@link ForecastEntry}s
 *
 * @param entries {@link List} of {@link ForecastEntry}s
 * @param statistics {@link ForecastStatistics}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record ForecastSummary(@Getter List<ForecastEntry> entries, @Getter ForecastStatistics statistics) {
}
