package com.traderbuddyv2.core.models.records;

import lombok.Getter;

import java.time.LocalDate;

/**
 * A class representation of a trade plan entry. This refers to a period of time that includes an earnings target, deposit and withdrawal amounts
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record TradingPlanEntry(@Getter LocalDate start, @Getter LocalDate end, @Getter double target, @Getter double credits, @Getter double debit) {
}
