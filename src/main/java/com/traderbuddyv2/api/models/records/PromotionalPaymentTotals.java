package com.traderbuddyv2.api.models.records;

import com.traderbuddyv2.core.models.entities.trade.Trade;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

public record PromotionalPaymentTotals(
        @Getter
        double total,
        @Getter
        int numberOfPayments,
        @Getter
        LocalDate lastPaymentDate
) {

    public PromotionalPaymentTotals(final List<Trade> trades) {
        this(trades.stream().mapToDouble(Trade::getNetProfit).sum(), trades.size(), trades.isEmpty() ? null : trades.get(0).getTradeOpenTime().toLocalDate());
    }
}
