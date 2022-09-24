package com.stephenprizio.traderbuddy.models.entities.trades;

import com.stephenprizio.traderbuddy.enums.trades.TradeType;
import com.stephenprizio.traderbuddy.enums.trades.TradingPlatform;
import com.stephenprizio.traderbuddy.models.entities.GenericEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Class representation of a trade taken in the market
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Entity
@Table(name = "trades")
public class Trade implements GenericEntity {

    @Id
    @Getter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(unique = true)
    private String tradeId;

    @Getter
    @Setter
    @Column
    private String product;

    @Getter
    @Setter
    @Column
    private TradingPlatform tradingPlatform;

    @Getter
    @Setter
    @Column
    private TradeType tradeType;

    @Getter
    @Setter
    @Column
    private LocalDateTime tradeOpenTime;

    @Getter
    @Setter
    @Column
    private LocalDateTime tradeCloseTime;

    @Getter
    @Setter
    @Column
    private Double lotSize;

    @Getter
    @Setter
    @Column
    private Double openPrice;

    @Getter
    @Setter
    @Column
    private Double closePrice;

    @Getter
    @Setter
    @Column
    private Double netProfit;

    @Getter
    @Setter
    @Column
    private String reasonForEntrance;

    @Getter
    @Setter
    @Column
    private String resultOfTrade;

    @Getter
    @Setter
    @Column
    private Boolean relevant = true;
}
