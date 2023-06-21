package com.traderbuddyv2.api.models.records.account;

import lombok.Getter;

/**
 * A record that represents a pair of values with option symbol
 *
 * @param code code
 * @param label display value
 * @param symbol display symbol
 * @author Stephen Prizio
 * @version 1.0
 */
public record PairEntry(@Getter Object code, @Getter Object label, @Getter String symbol) {}
