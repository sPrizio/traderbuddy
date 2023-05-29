package com.traderbuddyv2.integration.models.dto.forexfactory;

import com.traderbuddyv2.core.enums.news.MarketNewsSeverity;
import com.traderbuddyv2.core.enums.system.Country;
import com.traderbuddyv2.integration.models.dto.GenericIntegrationDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalTime;

/**
 * Class representation of an entry of news in a news day
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class CalendarNewsDayEntryDTO implements GenericIntegrationDTO {

    @Getter
    @Setter
    private String title;

    @Getter
    @Setter
    private Country country;

    @Getter
    @Setter
    private LocalTime time;

    @Getter
    @Setter
    private MarketNewsSeverity impact;

    @Getter
    @Setter
    private String forecast;

    @Getter
    @Setter
    private String previous;


    //  METHODS

    @Override
    public boolean isEmpty() {
        return StringUtils.isEmpty(title) || country == null;
    }
}
