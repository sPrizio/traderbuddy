package com.traderbuddyv2.integration.translators.forexfactory;

import com.traderbuddyv2.core.enums.news.MarketNewsSeverity;
import com.traderbuddyv2.core.enums.system.Country;
import com.traderbuddyv2.integration.models.dto.forexfactory.CalendarNewsDayEntryDTO;
import com.traderbuddyv2.integration.models.response.forexfactory.CalendarNewsEntryResponse;
import com.traderbuddyv2.integration.translators.GenericTranslator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Translates a {@link CalendarNewsEntryResponse}s into a {@link CalendarNewsDayEntryDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("calendarNewsDayEntryTranslator")
public class CalendarNewsDayEntryTranslator implements GenericTranslator<CalendarNewsEntryResponse, CalendarNewsDayEntryDTO> {


    //  METHODS

    @Override
    public CalendarNewsDayEntryDTO translate(final CalendarNewsEntryResponse response) {

        final CalendarNewsDayEntryDTO calendarNewsDayEntryDTO = new CalendarNewsDayEntryDTO();

        if (response != null && !response.isEmpty()) {
            calendarNewsDayEntryDTO.setCountry(Country.getByCurrencyCode(response.country()));
            calendarNewsDayEntryDTO.setTime(LocalDateTime.parse(response.date(), DateTimeFormatter.ISO_DATE_TIME).toLocalTime());
            calendarNewsDayEntryDTO.setForecast(response.forecast());
            calendarNewsDayEntryDTO.setTitle(response.title());
            calendarNewsDayEntryDTO.setImpact(MarketNewsSeverity.getFromDescription(response.impact()));
            calendarNewsDayEntryDTO.setPrevious(response.getPrevious());
        }

        return calendarNewsDayEntryDTO;
    }
}
