package com.traderbuddyv2.integration.models.response.forexfactory;

import com.traderbuddyv2.integration.models.response.GenericIntegrationResponse;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * Class representation of a single news entry in a particular day
 *
 * @param title title of event
 * @param country country that it affects
 * @param date date of news
 * @param impact severity (likelihood of market movement)
 * @param forecast expected value
 * @param previous previously reported value
 * @author Stephen Prizio
 * @version 1.0
 */
public record CalendarNewsEntryResponse(
        @Getter
        String title,
        @Getter
        String country,
        @Getter
        String date,
        @Getter
        String impact,
        @Getter
        String forecast,
        @Getter
        String previous
) implements GenericIntegrationResponse, Comparable<CalendarNewsEntryResponse> {


    //  METHODS

    @Override
    public boolean isEmpty() {
        return StringUtils.isEmpty(this.title) || StringUtils.isEmpty(this.country) || StringUtils.isEmpty(this.date);
    }

    @Override
    public int compareTo(CalendarNewsEntryResponse o) {
        return this.date.compareTo(o.date);
    }
}
