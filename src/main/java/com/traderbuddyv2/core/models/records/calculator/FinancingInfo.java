package com.traderbuddyv2.core.models.records.calculator;

import com.traderbuddyv2.core.enums.interval.AggregateInterval;

/**
 * Class representation of financing information for computing compounded interest
 *
 * @param principal starting value
 * @param interestRate interest rate expressed as a percentage
 * @param aggregateInterval {@link AggregateInterval}
 * @param period time period (number of times to apply compounding) in months
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record FinancingInfo(Double principal, Double interestRate, AggregateInterval aggregateInterval, Integer period) {
}
