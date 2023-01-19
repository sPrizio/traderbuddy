package com.traderbuddyv2.api.models.dto.news;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

/**
 * A DTO representation of a {@link MarketNewsSlotDTO}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class MarketNewsSlotDTO implements GenericDTO, Comparable<MarketNewsSlotDTO> {

    @Getter
    @Setter
    private String uid;

    @Getter
    @Setter
    private LocalTime time;

    @Getter
    @Setter
    private List<MarketNewsEntryDTO> entries;


    //  METHODS

    @Override
    public int compareTo(MarketNewsSlotDTO o) {
        return this.time.compareTo(o.time);
    }
}
