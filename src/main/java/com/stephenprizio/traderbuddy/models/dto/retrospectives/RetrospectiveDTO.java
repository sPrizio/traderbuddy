package com.stephenprizio.traderbuddy.models.dto.retrospectives;

import com.stephenprizio.traderbuddy.models.dto.GenericDTO;
import com.stephenprizio.traderbuddy.models.entities.retrospectives.Retrospective;
import lombok.Getter;
import lombok.Setter;

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
    private LocalDate startDate;

    @Getter
    @Setter
    private LocalDate endDate;

    @Getter
    @Setter
    private List<RetrospectiveEntryDTO> points;


    //  METHODS

    @Override
    public Boolean isEmpty() {
        return this.startDate == null && this.endDate == null;
    }
}
