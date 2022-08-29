package com.stephenprizio.traderbuddy.models.records.reporting;

import java.time.LocalDateTime;

/**
 * Represents a summary of trades for a particular time span
 *
 * @param date             {@link LocalDateTime} of period
 * @param target           target goal for profit
 * @param numberOfTrades   number of trades taken
 * @param winPercentage    number of trades won expressed as a percentage between 0 & 100
 * @param netProfit        net profit of trades (can be negative)
 * @param percentageProfit profit as a percentage of target
 * @param surplus          net difference between target and profit
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
public record TradingRecord(LocalDateTime date, Double target, Integer numberOfTrades, Integer winPercentage, Double netProfit, Double percentageProfit, Double surplus) {
}
