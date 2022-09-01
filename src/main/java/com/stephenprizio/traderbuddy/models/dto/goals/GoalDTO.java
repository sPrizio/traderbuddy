package com.stephenprizio.traderbuddy.models.dto.goals;

import com.stephenprizio.traderbuddy.enums.goals.GoalStatus;
import com.stephenprizio.traderbuddy.models.dto.GenericDTO;
import com.stephenprizio.traderbuddy.models.entities.goals.Goal;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

/**
 * A DTO representation of a {@link Goal}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class GoalDTO implements GenericDTO {

    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private LocalDate startDate;

    @Getter
    @Setter
    private LocalDate endDate;

    @Getter
    @Setter
    private Double profitTarget;

    @Getter
    @Setter
    private Boolean active;

    @Getter
    @Setter
    private GoalStatus status;


    //  METHODS

    @Override
    public Boolean isEmpty() {
        return this.startDate == null && this.endDate == null;
    }
}
