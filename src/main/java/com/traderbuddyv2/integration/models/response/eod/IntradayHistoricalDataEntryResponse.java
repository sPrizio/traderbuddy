package com.traderbuddyv2.integration.models.response.eod;

import com.traderbuddyv2.integration.models.response.GenericIntegrationResponse;
import lombok.Getter;

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
