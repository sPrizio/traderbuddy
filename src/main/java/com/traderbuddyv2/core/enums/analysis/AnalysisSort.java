package com.traderbuddyv2.core.enums.analysis;

import com.traderbuddyv2.core.models.nonentities.analysis.TradePerformance;
import lombok.Getter;

import java.util.Comparator;

/**
 * Enum representing possible sorts for {@link TradePerformance}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum AnalysisSort {

    CLOSE_TIME(Comparator.comparing(TradePerformance::getTradeCloseTime)),
    LOT_SIZE(Comparator.comparing(TradePerformance::getLotSize)),
    NET_PROFIT(Comparator.comparing(TradePerformance::getNetProfit)),
    PIPS(Comparator.comparing(TradePerformance::getPips)),
    TRADE_DURATION(Comparator.comparing(TradePerformance::getTradeDuration));

    @Getter
    private final Comparator<TradePerformance> sort;

    AnalysisSort(final Comparator<TradePerformance> sort) {
        this.sort = sort;
    }
}
