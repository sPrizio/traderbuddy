package com.traderbuddyv2.core.models.nonentities.trade;

import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.models.nonentities.analysis.Analysis;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * Class representation of basic trade totals
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
public class TradeTotals extends Analysis {

    @Getter
    @Setter
    private int totalTrades;

    @Getter
    @Setter
    private int winningTrades;

    @Getter
    @Setter
    private int losingTrades;

    @Getter
    @Setter
    private int winPercentage;

    @Getter
    @Setter
    private double netProfit;

    @Getter
    @Setter
    private double netPoints;


    //  CONSTRUCTORS

    public TradeTotals(final List<Trade> trades) {
        this.totalTrades = trades.size();
        this.winningTrades = (int) trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d >= 0.0).count();
        this.losingTrades = (int) trades.stream().mapToDouble(Trade::getNetProfit).filter(d -> d < 0.0).count();
        this.winPercentage = this.totalTrades == 0 ? 0 : (int) (((double) this.winningTrades / (double) this.totalTrades) * 100.0);
        this.netProfit = trades.stream().mapToDouble(Trade::getNetProfit).sum();
        this.netPoints = trades.stream().mapToDouble(this::getPips).sum();
    }
}
