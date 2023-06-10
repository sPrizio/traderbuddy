package com.traderbuddyv2.core.enums.trade.platform;

import lombok.Getter;

/**
 * Enum representing different trading platforms
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public enum TradePlatform {
    CMC_MARKETS(".csv"),
    METATRADER4(".html", ".htm"),
    METATRADER5(".html", ".htm"),
    CTRADER(".csv"),
    UNDEFINED();

    @Getter
    private final String[] formats;

    TradePlatform(final String... formats) {
        this.formats = formats;
    }
}
