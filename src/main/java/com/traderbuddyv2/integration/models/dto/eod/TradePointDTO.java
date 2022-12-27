package com.traderbuddyv2.integration.models.dto.eod;

import com.traderbuddyv2.core.enums.trades.TradeType;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.integration.models.dto.GenericIntegrationDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Class representation of a trade point. Could be an entry or exit
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradePointDTO implements GenericIntegrationDTO {

    @Getter
    @Setter
    private LocalDateTime x;

    @Getter
    @Setter
    private double y;

    @Getter
    @Setter
    private boolean entry;

    @Getter
    @Setter
    private boolean win;

    @Getter
    @Setter
    private String label;

    //  CONSTRUCTORS

    public TradePointDTO(final Trade trade, final boolean entry) {
        this.x = truncateMinutes(trade, entry);
        this.y = entry ? trade.getOpenPrice() : trade.getClosePrice();
        this.entry = entry;
        this.win = trade.getNetProfit() >= 0.0;
        this.label = entry ? trade.getTradeType().equals(TradeType.BUY) ? "Long" : "Short" : "Exit";
    }


    //  METHODS

    @Override
    public boolean isEmpty() {
        return this.x == null;
    }


    //  HELPERS

    /**
     * Keeps the entries and exits locked to the time frame they're on (hard coded to 5 minutes for now
     *
     * @param trade {@link Trade}
     * @param entry entry flag
     * @return modified {@link LocalDateTime}
     */
    private LocalDateTime truncateMinutes(final Trade trade, final boolean entry) {

        LocalDateTime dateTime = entry ? trade.getTradeOpenTime().withSecond(0) : trade.getTradeCloseTime().withSecond(0);

        double minute = dateTime.getMinute();
        dateTime = dateTime.withMinute((int) (5 * (Math.floor(minute / 5))));

        return dateTime;
    }
}
