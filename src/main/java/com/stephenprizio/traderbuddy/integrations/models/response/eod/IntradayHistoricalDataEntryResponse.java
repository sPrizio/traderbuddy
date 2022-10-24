package com.stephenprizio.traderbuddy.integrations.models.response.eod;

import com.stephenprizio.traderbuddy.integrations.models.response.GenericIntegrationResponse;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Class representation of a intraday historical data from EOD
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record IntradayHistoricalDataEntryResponse(
        @Getter long timestamp,
        @Getter int gmtoffset,
        @Getter String datetime,
        @Getter double open,
        @Getter double high,
        @Getter double low,
        @Getter double close,
        @Getter double volume
) implements GenericIntegrationResponse {


    //  METHODS

    @Override
    public boolean isEmpty() {
        return this.timestamp == 0 && this.datetime == null;
    }
}
