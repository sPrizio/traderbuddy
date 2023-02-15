package com.traderbuddyv2.core.models.entities.trade;

import com.traderbuddyv2.core.enums.trade.info.TradeType;
import com.traderbuddyv2.core.enums.trade.platform.TradePlatform;
import com.traderbuddyv2.core.enums.trade.tag.TradeEntryReason;
import com.traderbuddyv2.core.enums.trade.tag.TradeResult;
import com.traderbuddyv2.core.models.entities.GenericEntity;
import com.traderbuddyv2.core.models.entities.account.Account;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

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
    private TradePlatform tradePlatform;

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
    private double lotSize;

    @Getter
    @Setter
    @Column
    private double openPrice;

    @Getter
    @Setter
    @Column
    private double closePrice;

    @Getter
    @Setter
    @Column
    private double netProfit;

    @Getter
    @Setter
    @ElementCollection
    private List<TradeEntryReason> reasonsForEntry;

    @Getter
    @Setter
    @ElementCollection
    private List<TradeResult> resultsOfTrade;

    @Getter
    @Setter
    @Column
    private boolean relevant = true;

    @Getter
    @Setter
    @Column
    private boolean processed = false;

    @Getter
    @Setter
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Account account;

}