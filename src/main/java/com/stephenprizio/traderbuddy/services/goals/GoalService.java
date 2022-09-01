package com.stephenprizio.traderbuddy.services.goals;

import com.stephenprizio.traderbuddy.enums.goals.GoalStatus;
import com.stephenprizio.traderbuddy.models.entities.goals.Goal;
import com.stephenprizio.traderbuddy.repositories.goals.GoalRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateIfSingleResult;
import static com.stephenprizio.traderbuddy.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link Goal}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("goalService")
public class GoalService {

    @Resource(name = "goalRepository")
    private GoalRepository goalRepository;


    //  METHODS

    /**
     * Returns an {@link Optional} {@link Goal} that is currently active. Note that the system only supports 1 active goal at a time
     *
     * @return {@link Optional} {@link Goal}
     */
    public Optional<Goal> findCurrentlyActiveGoal() {
        List<Goal> goals = this.goalRepository.findGoalByActiveIsTrue();

        validateIfSingleResult(goals, "One or more active goals was found. This is not allowed. Only 1 goal can be active");

        return Optional.of(goals.get(0));
    }

    /**
     * Returns a {@link List} of {@link Goal}s by their {@link GoalStatus}
     *
     * @param status {@link GoalStatus}
     * @return {@link List} of {@link Goal}s
     */
    public List<Goal> findGoalsForStatus(GoalStatus status) {
        validateParameterIsNotNull(status, "goal status cannot be null");
        return this.goalRepository.findAllByStatus(status);
    }
}
