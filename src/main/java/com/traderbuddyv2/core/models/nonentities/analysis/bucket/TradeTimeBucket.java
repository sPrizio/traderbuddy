package com.traderbuddyv2.core.models.nonentities.analysis.bucket;

import com.traderbuddyv2.core.enums.trade.platform.TradePlatform;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

/**
 * Class representation of a breakdown of trades for a given time span
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradeTimeBucket extends Bucket {

    @Getter
    @Setter
    private LocalTime start;

    @Getter
    @Setter
    private LocalTime end;


    //  CONSTRUCTORS

    public TradeTimeBucket(final LocalTime start, final LocalTime end, final List<Trade> trades, final TradePlatform tradePlatform) {
        super(trades);

        if (tradePlatform.equals(TradePlatform.METATRADER4)) {
            this.start = start.minusHours(7);
            this.end = end.minusHours(7);
        } else {
            this.start = start;
            this.end = end;
        }
    }
}
