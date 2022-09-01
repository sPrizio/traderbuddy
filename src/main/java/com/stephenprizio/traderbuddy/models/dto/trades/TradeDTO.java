package com.stephenprizio.traderbuddy.models.dto.trades;

import com.stephenprizio.traderbuddy.enums.trades.TradeType;
import com.stephenprizio.traderbuddy.enums.trades.TradingPlatform;
import com.stephenprizio.traderbuddy.models.dto.GenericDTO;
import com.stephenprizio.traderbuddy.models.entities.trades.Trade;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDateTime;

/**
 * A DTO representation of a {@link Trade}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradeDTO implements GenericDTO {

    @Getter
    @Setter
    private String tradeId;

    @Getter
    @Setter
    private TradingPlatform tradingPlatform;

    @Getter
    @Setter
    private TradeType tradeType;

    @Getter
    @Setter
    private String product;

    @Getter
    @Setter
    private LocalDateTime tradeOpenTime;

    @Getter
    @Setter
    private LocalDateTime tradeCloseTime;

    @Getter
    @Setter
    private Double lotSize;

    @Getter
    @Setter
    private Double openPrice;

    @Getter
    @Setter
    private Double closePrice;

    @Getter
    @Setter
    private Double netProfit;

    @Getter
    @Setter
    private String reasonForEntrance;

    @Getter
    @Setter
    private String resultOfTrade;

    @Getter
    @Setter
    private Boolean relevant;


    //  METHODS

    @Override
    public Boolean isEmpty() {
        return StringUtils.isEmpty(this.tradeId);
    }
}
