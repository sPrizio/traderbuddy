package com.traderbuddyv2.core.models.records;

import lombok.Getter;

import java.time.LocalDate;

/**
 * Class representation of a point on an equity curve
 *
 * @param date {@link LocalDate}
 * @param value {@link Double}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record EquityCurveEntry(@Getter LocalDate date, @Getter double value) {
}
