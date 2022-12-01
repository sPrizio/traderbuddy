package com.traderbuddyv2.integration.translators.eod;

import com.traderbuddyv2.integration.models.dto.eod.IntradayHistoricalDataDTO;
import com.traderbuddyv2.integration.models.response.eod.IntradayHistoricalDataResponse;
import com.traderbuddyv2.integration.translators.GenericTranslator;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * Translates {@link IntradayHistoricalDataResponse}s into {@link IntradayHistoricalDataDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("intradayHistoricalDataTranslator")
public class IntradayHistoricalDataTranslator implements GenericTranslator<IntradayHistoricalDataResponse, IntradayHistoricalDataDTO> {

    @Resource(name = "intradayHistoricalDataEntryTranslator")
    private IntradayHistoricalDataEntryTranslator intradayHistoricalDataEntryTranslator;


    //  METHODS

    @Override
    public IntradayHistoricalDataDTO translate(final IntradayHistoricalDataResponse response) {

        if (response != null && !response.isEmpty()) {
            IntradayHistoricalDataDTO intradayHistoricalDataDTO = new IntradayHistoricalDataDTO();
            intradayHistoricalDataDTO.setEntries(this.intradayHistoricalDataEntryTranslator.translateAll(response.entries()));
            return intradayHistoricalDataDTO;
        }

        return null;
    }
}
