package com.stephenprizio.traderbuddy.integrations.models.dto.eod;

import com.stephenprizio.traderbuddy.integrations.models.dto.GenericIntegrationDTO;
import lombok.Getter;
import lombok.Setter;

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
        return false;
    }
}
