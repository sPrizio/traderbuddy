package com.traderbuddyv2.integration.models.response.eod;

import com.traderbuddyv2.integration.models.response.GenericIntegrationResponse;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * Class representation of a collection of historical intraday data from EOD
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public record IntradayHistoricalDataResponse(
        @Getter LocalDate date,
        @Getter String symbol,
        @Getter String name,
        @Getter List<IntradayHistoricalDataEntryResponse> entries
) implements GenericIntegrationResponse {


    //  METHODS

    @Override
    public boolean isEmpty() {
        return CollectionUtils.isEmpty(this.entries);
    }
}
