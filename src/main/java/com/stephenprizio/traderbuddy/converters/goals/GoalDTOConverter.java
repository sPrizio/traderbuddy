package com.stephenprizio.traderbuddy.converters.goals;

import com.stephenprizio.traderbuddy.converters.GenericDTOConverter;
import com.stephenprizio.traderbuddy.models.dto.goals.GoalDTO;
import com.stephenprizio.traderbuddy.models.entities.goals.Goal;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Converter for {@link Goal}s into {@link GoalDTO}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("goalDTOConverter")
public class GoalDTOConverter implements GenericDTOConverter<Goal, GoalDTO> {


    //  METHODS

    @Override
    public GoalDTO convert(Goal entity) {

        if (entity == null) {
            return new GoalDTO();
        }

        GoalDTO goalDTO = new GoalDTO();

        goalDTO.setActive(entity.getActive());
        goalDTO.setName(entity.getName());
        goalDTO.setStartDate(entity.getStartDate());
        goalDTO.setEndDate(entity.getEndDate());
        goalDTO.setProfitTarget(entity.getProfitTarget());
        goalDTO.setStatus(entity.getStatus());
        goalDTO.setCompoundFrequency(entity.getCompoundFrequency());
        goalDTO.setStartingBalance(entity.getStartingBalance());

        return goalDTO;
    }

    @Override
    public List<GoalDTO> convertAll(List<Goal> entities) {
        return entities.stream().map(this::convert).toList();
    }
}
