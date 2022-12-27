package com.traderbuddyv2.integration.models.dto.eod;

import com.traderbuddyv2.integration.models.dto.GenericIntegrationDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
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
    private LocalDate date;

    @Getter
    @Setter
    private String product;

    @Getter
    @Setter
    private String symbol;

    @Getter
    @Setter
    private int offset;

    @Getter
    @Setter
    private List<IntradayHistoricalDataEntryDTO> entries;

    @Getter
    @Setter
    private List<TradePointDTO> points;


    //  METHODS

    @Override
    public boolean isEmpty() {
        return CollectionUtils.isEmpty(this.entries);
    }
}
