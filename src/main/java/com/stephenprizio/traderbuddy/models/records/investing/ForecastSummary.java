package com.stephenprizio.traderbuddy.models.records.investing;

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
public record ForecastSummary(List<ForecastEntry> entries, ForecastStatistics statistics) {
}
