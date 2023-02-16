package com.traderbuddyv2.core.models.nonentities.trade;

import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.models.nonentities.analysis.Analysis;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Class representing totals for {@link Trade}s marked as non-relevant
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class IrrelevantTradeTotals extends Analysis {

    @Getter
    @Setter
    private TradeTotals current;

    @Getter
    @Setter
    private TradeTotals previous;


    //  CONSTRUCTORS

    public IrrelevantTradeTotals(final List<Trade> current, final List<Trade> previous) {
        this.current = new TradeTotals(current);
        this.previous = new TradeTotals(previous);
    }
}
