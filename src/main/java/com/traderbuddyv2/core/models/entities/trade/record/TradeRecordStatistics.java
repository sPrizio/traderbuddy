package com.traderbuddyv2.core.models.entities.trade.record;

import com.traderbuddyv2.core.models.entities.GenericEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

/**
 * Class representation of statistics for a {@link TradeRecord}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "trade_records_statistics")
public class TradeRecordStatistics implements GenericEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column
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
    @Column
    private double netProfit;

    @Getter
    @Setter
    @Column
    private double percentageProfit;

    @Getter
    @Setter
    @Column
    private int winPercentage;

    @Getter
    @Setter
    @Column
    private double averageWinAmount;

    @Getter
    @Setter
    @Column
    private double averageWinSize;

    @Getter
    @Setter
    @Column
    private double averageLossAmount;

    @Getter
    @Setter
    @Column
    private double averageLossSize;

    @Getter
    @Setter
    @Column
    private double largestWinAmount;

    @Getter
    @Setter
    @Column
    private double largestWinSize;

    @Getter
    @Setter
    @Column
    private double largestLossAmount;

    @Getter
    @Setter
    @Column
    private double largestLossSize;

    @Getter
    @Setter
    @Column
    private double tradingRate;

    @Getter
    @Setter
    @OneToOne(mappedBy = "statistics", cascade = CascadeType.ALL)
    private TradeRecord tradeRecord;
}
