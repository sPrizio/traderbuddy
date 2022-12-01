package com.traderbuddyv2.integration.models.dto.eod;

import com.traderbuddyv2.integration.models.dto.GenericIntegrationDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;

/**
 * DTO representation of intraday historical data
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class IntradayHistoricalDataDTO implements GenericIntegrationDTO {

    @Getter
    @Setter
    private List<IntradayHistoricalDataEntryDTO> entries;


    //  METHODS

    @Override
    public boolean isEmpty() {
        return CollectionUtils.isEmpty(this.entries);
    }
}
