package com.traderbuddyv2.integration.models.dto.eod;

import com.traderbuddyv2.integration.models.dto.GenericIntegrationDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * DTO representation an entry of intraday historical data
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class IntradayHistoricalDataEntryDTO implements GenericIntegrationDTO {

    @Getter
    @Setter
    private LocalDateTime datetime;

    @Getter
    @Setter
    private double open;

    @Getter
    @Setter
    private double high;

    @Getter
    @Setter
    private double low;

    @Getter
    @Setter
    private double close;

    @Getter
    @Setter
    private double volume;


    //  METHODS

    @Override
    public boolean isEmpty() {
        return this.datetime != null;
    }
}
