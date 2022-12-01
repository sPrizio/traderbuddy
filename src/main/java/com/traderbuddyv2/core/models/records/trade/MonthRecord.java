package com.traderbuddyv2.core.models.records.trade;

import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import lombok.Getter;

import java.time.Month;

/**
 * A class representation of {@link TradeRecord}s for 1 calendar month. Used as a quick overview of basic performance for the time span
 *
 * @param month {@link Month}
 * @param active true if this month has {@link TradeRecord}s
 * @param numberOfTrades number of trades taken
 * @param netProfit net P & L
 */
public record MonthRecord(
        @Getter Month month,
        @Getter boolean active,
        @Getter int numberOfTrades,
        @Getter double netProfit
) {}
