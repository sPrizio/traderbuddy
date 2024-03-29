package com.traderbuddyv2.api.models.dto.trade;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.api.models.dto.account.AccountDTO;
import com.traderbuddyv2.api.models.records.wrapper.TradeReasonWrapper;
import com.traderbuddyv2.api.models.records.wrapper.TradeResultWrapper;
import com.traderbuddyv2.core.enums.trade.info.TradeType;
import com.traderbuddyv2.core.enums.trade.platform.TradePlatform;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

/**
 * A DTO representation of a {@link Trade}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradeDTO implements GenericDTO {

    @Getter
    @Setter
    public String uid;

    @Getter
    @Setter
    private String tradeId;

    @Getter
    @Setter
    private String product;

    @Getter
    @Setter
    private TradePlatform tradePlatform;

    @Getter
    @Setter
    private TradeType tradeType;

    @Getter
    @Setter
    private LocalDateTime tradeOpenTime;

    @Getter
    @Setter
    private LocalDateTime tradeCloseTime;

    @Getter
    @Setter
    private double lotSize;

    @Getter
    @Setter
    private double openPrice;

    @Getter
    @Setter
    private double closePrice;

    @Getter
    @Setter
    private double netProfit;

    @Getter
    @Setter
    private double pips;

    @Getter
    @Setter
    private List<TradeReasonWrapper> reasonForEntrance;

    @Getter
    @Setter
    private List<TradeResultWrapper> resultOfTrade;

    @Getter
    @Setter
    private double stopLoss;

    @Getter
    @Setter
    private double takeProfit;

    @Getter
    @Setter
    private boolean relevant;

    @Getter
    @Setter
    private boolean processed;

    @Getter
    @Setter
    private AccountDTO account;
}

