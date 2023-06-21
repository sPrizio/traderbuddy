package com.traderbuddyv2.core.enums.trade.platform;

import lombok.Getter;

/**
 * Enum representing different trading platforms
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum TradePlatform {
    CMC_MARKETS("CMC_MARKETS", "CMC Markets", ".csv"),
    METATRADER4("METATRADER4", "MetaTrader 4", ".html", ".htm"),
    METATRADER5("METATRADER5", "MetaTrader 5", ".html", ".htm"),
    CTRADER("CTRADER", "CTrader", ".csv"),
    UNDEFINED("N/A", "N/A");

    @Getter
    private final String code;

    @Getter
    private final String label;

    @Getter
    private final String[] formats;

    TradePlatform(final String code, final String label, final String... formats) {
        this.code = code;
        this.label = label;
        this.formats = formats;
    }
}
