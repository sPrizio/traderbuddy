package com.traderbuddyv2.integration.services.forexfactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traderbuddyv2.integration.client.forexfactory.ForexFactoryIntegrationClient;
import com.traderbuddyv2.integration.exceptions.IntegrationException;
import com.traderbuddyv2.integration.models.dto.forexfactory.CalendarNewsDayDTO;
import com.traderbuddyv2.integration.models.response.forexfactory.CalendarNewsEntryResponse;
import com.traderbuddyv2.integration.services.GenericIntegrationService;
import com.traderbuddyv2.integration.translators.forexfactory.CalendarNewsDayEntryTranslator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Forex Factory API implementation of {@link GenericIntegrationService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("forexFactoryIntegrationService")
public class ForexFactoryIntegrationService implements GenericIntegrationService {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${forex.factory.calendar.url}")
    private String calendarUrl;

    @Resource(name = "calendarNewsDayEntryTranslator")
    private CalendarNewsDayEntryTranslator calendarNewsDayEntryTranslator;

    @Resource(name = "forexFactoryIntegrationClient")
    private ForexFactoryIntegrationClient forexFactoryIntegrationClient;


    //  METHODS

    /**
     * Obtains the current week's news
     *
     * @return {@link List} of {@link CalendarNewsDayDTO}
     */
    public List<CalendarNewsDayDTO> getCurrentWeekNews() {

        String response = this.forexFactoryIntegrationClient.get(this.calendarUrl, new LinkedMultiValueMap<>());
        if (StringUtils.isEmpty(response)) {
            throw new IntegrationException(String.format("An error occurred while connecting to %s", this.calendarUrl));
        }

        try {
            final CalendarNewsEntryResponse[] entries = this.objectMapper.readValue(response, CalendarNewsEntryResponse[].class);
            final Map<LocalDate, List<CalendarNewsEntryResponse>> map = generateDataMap(entries);
            final List<CalendarNewsDayDTO> news = new ArrayList<>();

            map.forEach((key, value) -> {
                final CalendarNewsDayDTO day = new CalendarNewsDayDTO();
                day.setDate(key);
                day.setEntries(this.calendarNewsDayEntryTranslator.translateAll(value));
                news.add(day);
            });

            return news.stream().sorted(Comparator.comparing(CalendarNewsDayDTO::getDate)).toList();
        } catch (Exception e) {
            throw new IntegrationException("The response came in an unexpected format", e);
        }
    }


    //  HELPERS

    /**
     * Generates a {@link Map} of key-value pairs of {@link String} and {@link List} {@link CalendarNewsEntryResponse}s for the given list of {@link CalendarNewsEntryResponse}
     * The idea is to create a map of week days and populate each week day with all news entries for that day
     *
     * @param entries array of {@link CalendarNewsEntryResponse}
     * @return {@link Map}
     */
    private Map<LocalDate, List<CalendarNewsEntryResponse>> generateDataMap(final CalendarNewsEntryResponse[] entries) {

        final Map<LocalDate, List<CalendarNewsEntryResponse>> map = new HashMap<>();
        if (entries != null) {
            for (CalendarNewsEntryResponse data : entries) {
                final List<CalendarNewsEntryResponse> list;
                if (map.containsKey(getDateTime(data.date()).toLocalDate())) {
                    list = new ArrayList<>(map.get(getDateTime(data.date()).toLocalDate()));
                } else {
                    list = new ArrayList<>();
                }

                list.add(data);
                map.put(getDateTime(data.date()).toLocalDate(), list.stream().sorted(Comparator.comparing(CalendarNewsEntryResponse::date)).toList());
            }
        }

        return map;
    }

    /**
     * Obtains a {@link LocalDateTime} from the given string
     *
     * @param string date time string
     * @return {@link LocalDateTime}
     */
    private LocalDateTime getDateTime(final String string) {
        try {
            return LocalDateTime.parse(string, DateTimeFormatter.ISO_DATE_TIME);
        } catch (Exception e) {
            return LocalDateTime.now();
        }
    }
}
