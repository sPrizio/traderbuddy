package com.traderbuddyv2.api.models.dto.news;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.core.models.entities.news.MarketNewsEntry;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO representation of  {@link MarketNewsEntry}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class MarketNewsEntryDTO implements GenericDTO, Comparable<MarketNewsEntryDTO> {

    @Getter
    @Setter
    private String uid;

    @Getter
    @Setter
    private String content;

    @Getter
    @Setter
    private String severity;

    @Getter
    @Setter
    private int severityLevel;

    @Getter
    @Setter
    private String country;


    //  METHODS

    @Override
    public int compareTo(MarketNewsEntryDTO o) {
        return this.content.compareTo(o.content);
    }
}
