package com.traderbuddyv2.api.models.dto.news;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.core.models.entities.news.MarketNews;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

/**
 * A DTO representation of a {@link MarketNews}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class MarketNewsDTO implements GenericDTO, Comparable<MarketNewsDTO> {

    @Getter
    @Setter
    private String uid;

    @Getter
    @Setter
    private LocalDate date;

    @Getter
    @Setter
    private List<MarketNewsSlotDTO> slots;

    @Getter
    @Setter
    private boolean active;

    @Getter
    @Setter
    private boolean past;

    @Getter
    @Setter
    private boolean future;


    //  METHODS
    @Override
    public int compareTo(MarketNewsDTO o) {
        return 0;
    }
}
