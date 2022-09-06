package com.stephenprizio.traderbuddy.models.records.calculator;

/**
 * Class representation of a record of compound interest calculations
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record CompoundedInterestRecord(Integer periodIndex, Double interest, Double accruedInterest, Double balance) {
}
