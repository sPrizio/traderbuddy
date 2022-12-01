package com.traderbuddyv2.api.models.records;

import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Class representation of a point on an equity curve for intraday statistics
 *
 * @param x {@link LocalDateTime} x value indicating time
 * @param y {@link Double} y value indicating price
 * @author Stephen Prizio
 * @version 1.0
 */
public record IntradayEquityCurvePoint(@Getter LocalDateTime x, @Getter double y) {
}
