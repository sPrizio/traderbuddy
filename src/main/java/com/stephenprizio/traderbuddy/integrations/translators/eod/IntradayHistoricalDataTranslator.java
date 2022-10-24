package com.stephenprizio.traderbuddy.integrations.translators.eod;

import com.stephenprizio.traderbuddy.integrations.models.dto.eod.IntradayHistoricalDataDTO;
import com.stephenprizio.traderbuddy.integrations.models.response.eod.IntradayHistoricalDataResponse;
import com.stephenprizio.traderbuddy.integrations.translators.GenericTranslator;
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
