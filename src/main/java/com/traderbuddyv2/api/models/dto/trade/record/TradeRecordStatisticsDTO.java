package com.traderbuddyv2.api.models.dto.trade.record;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.api.models.records.misc.IntradayEquityCurvePoint;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecordStatistics;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.util.List;

/**
 * A DTO representation of a {@link TradeRecordStatistics}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradeRecordStatisticsDTO implements GenericDTO {

    @Getter
    @Setter
    private String uid;

    @Getter
    @Setter
    private int numberOfTrades;

    @Getter
    @Setter
    @Column
    private int numberOfWinningTrades;

    @Getter
    @Setter
    @Column
    private int numberOfLosingTrades;

    @Getter
    @Setter
    private double netProfit;

    @Getter
    @Setter
    private double percentageProfit;

    @Getter
    @Setter
    private int winPercentage;

    @Getter
    @Setter
    private double grossWinAmount;

    @Getter
    @Setter
    private double grossLossAmount;

    @Getter
    @Setter
    private double averageWinAmount;

    @Getter
    @Setter
    private double averageWinSize;

    @Getter
    @Setter
    private double averageWinDelta;

    @Getter
    @Setter
    private double averageLossAmount;

    @Getter
    @Setter
    private double averageLossSize;

    @Getter
    @Setter
    private double averageLossDelta;

    @Getter
    @Setter
    private double largestWinAmount;

    @Getter
    @Setter
    private double largestWinSize;

    @Getter
    @Setter
    private double largestWinDelta;

    @Getter
    @Setter
    private double largestLossAmount;

    @Getter
    @Setter
    private double largestLossSize;

    @Getter
    @Setter
    private double largestLossDelta;

    @Getter
    @Setter
    private double tradingRate;

    @Getter
    @Setter
    private double pipsEarned;

    @Getter
    @Setter
    private double pipsLost;

    @Getter
    @Setter
    private double netPips;

    @Getter
    @Setter
    private double profitability;

    @Getter
    @Setter
    private List<IntradayEquityCurvePoint> points;
}
