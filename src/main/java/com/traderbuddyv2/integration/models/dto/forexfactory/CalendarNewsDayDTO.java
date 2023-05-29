package com.traderbuddyv2.integration.models.dto.forexfactory;

import com.traderbuddyv2.integration.models.dto.GenericIntegrationDTO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections4.CollectionUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * Class representation of a day that contains news
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class CalendarNewsDayDTO implements GenericIntegrationDTO {

    @Getter
    @Setter
    private LocalDate date;

    @Getter
    @Setter
    private List<CalendarNewsDayEntryDTO> entries;


    //  METHODS

    @Override
    public boolean isEmpty() {
        return this.date == null || CollectionUtils.isEmpty(this.entries);
    }
}
