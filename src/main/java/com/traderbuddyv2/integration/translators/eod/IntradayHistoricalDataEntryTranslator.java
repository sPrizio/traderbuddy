package com.traderbuddyv2.integration.translators.eod;

import com.traderbuddyv2.integration.models.dto.eod.IntradayHistoricalDataEntryDTO;
import com.traderbuddyv2.integration.models.response.eod.IntradayHistoricalDataEntryResponse;
import com.traderbuddyv2.integration.translators.GenericTranslator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Translates {@link IntradayHistoricalDataEntryResponse}s into {@link IntradayHistoricalDataEntryDTO}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("intradayHistoricalDataEntryTranslator")
public class IntradayHistoricalDataEntryTranslator implements GenericTranslator<IntradayHistoricalDataEntryResponse, IntradayHistoricalDataEntryDTO> {


    //  METHODS

    @Override
    public IntradayHistoricalDataEntryDTO translate(final IntradayHistoricalDataEntryResponse response) {

        if (response != null && !response.isEmpty()) {
            IntradayHistoricalDataEntryDTO intradayHistoricalDataEntryDTO = new IntradayHistoricalDataEntryDTO();

            intradayHistoricalDataEntryDTO.setClose(response.getClose());
            intradayHistoricalDataEntryDTO.setHigh(response.getHigh());
            intradayHistoricalDataEntryDTO.setLow(response.getLow());
            intradayHistoricalDataEntryDTO.setOpen(response.getOpen());
            intradayHistoricalDataEntryDTO.setVolume(response.getVolume());
            intradayHistoricalDataEntryDTO.setDatetime(LocalDateTime.parse(response.getDatetime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            return intradayHistoricalDataEntryDTO;
        }

        return null;
    }
}
