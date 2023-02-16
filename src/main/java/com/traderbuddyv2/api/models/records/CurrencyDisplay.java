package com.traderbuddyv2.api.models.records;

import lombok.Getter;

/**
 * Record for displaying currency information
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record CurrencyDisplay(@Getter String isoCode, @Getter String label) {}
