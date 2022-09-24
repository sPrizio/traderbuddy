package com.stephenprizio.traderbuddy.models.dto.retrospectives;

import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import com.stephenprizio.traderbuddy.models.dto.GenericDTO;
import com.stephenprizio.traderbuddy.models.entities.retrospectives.Retrospective;
import com.stephenprizio.traderbuddy.models.records.reporting.retrospectives.RetrospectiveStatistics;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.time.LocalDate;
import java.util.List;

/**
 * A DTO representation of {@link Retrospective}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class RetrospectiveDTO implements GenericDTO {

    @Getter
    @Setter
    private String uid;

    @Getter
    @Setter
    private LocalDate startDate;

    @Getter
    @Setter
    private LocalDate endDate;

    @Getter
    @Setter
    private AggregateInterval intervalFrequency;

    @Getter
    @Setter
    private List<RetrospectiveEntryDTO> points;

    @Getter
    @Setter
    private RetrospectiveStatistics retrospectiveStatistics;


    //  METHODS

    @Override
    public Boolean isEmpty() {
        return StringUtils.isEmpty(this.uid);
    }
}
