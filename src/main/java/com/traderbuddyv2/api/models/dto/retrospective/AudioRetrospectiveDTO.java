package com.traderbuddyv2.api.models.dto.retrospective;

import com.traderbuddyv2.api.models.dto.GenericDTO;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.retrospective.AudioRetrospective;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * A DTO for {@link AudioRetrospective}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class AudioRetrospectiveDTO implements GenericDTO {

    @Getter
    @Setter
    public String uid;

    @Getter
    @Setter
    public LocalDate start;

    @Getter
    @Setter
    public LocalDate end;

    @Getter
    @Setter
    public String name;

    @Getter
    @Setter
    public String url;

    @Getter
    @Setter
    public AggregateInterval aggregateInterval;
}
