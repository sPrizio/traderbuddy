package com.stephenprizio.traderbuddy.models.records.calculator;

import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;

/**
 * Class representation of financing information for computing compounded interest
 *
 * @param principal starting value
 * @param interestRate interest rate expressed as a percentage
 * @param compoundFrequency {@link CompoundFrequency}
 * @param period time period (number of times to apply compounding) in months
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record FinancingInfo(Double principal, Double interestRate, CompoundFrequency compoundFrequency, Integer period) {
}
