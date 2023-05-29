package com.traderbuddyv2.integration.models.response.eod;

import com.traderbuddyv2.integration.models.response.GenericIntegrationResponse;
import lombok.Getter;

/**
 * Class representation of an intraday historical data from EOD
 *
 * @param timestamp timestamp
 * @param gmtoffset time zone offset
 * @param datetime date & time
 * @param open open price
 * @param high high price
 * @param low low price
 * @param close close price
 * @param volume volume for time period
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
