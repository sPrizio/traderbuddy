package com.stephenprizio.traderbuddy.services.goals;

import com.stephenprizio.traderbuddy.enums.goals.GoalStatus;
import com.stephenprizio.traderbuddy.exceptions.system.EntityCreationException;
import com.stephenprizio.traderbuddy.exceptions.system.EntityModificationException;
import com.stephenprizio.traderbuddy.exceptions.validation.MissingRequiredDataException;
import com.stephenprizio.traderbuddy.exceptions.validation.NoResultFoundException;
import com.stephenprizio.traderbuddy.models.entities.goals.Goal;
import com.stephenprizio.traderbuddy.repositories.goals.GoalRepository;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.stephenprizio.traderbuddy.validation.GenericValidator.*;

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

    /**
     * Returns an {@link Optional} {@link Goal} for the given name, start and end date
     *
     * @param name goal name
     * @param startDate {@link LocalDate} start date
     * @param endDate {@link LocalDate} end date
     * @return {@link Optional} {@link Goal}
     */
    public Optional<Goal> findGoalForNameAndStartDateAndEndDate(String name, LocalDate startDate, LocalDate endDate) {

        validateParameterIsNotNull(name, "name cannot be null");
        validateParameterIsNotNull(startDate, "startDate cannot be null");
        validateParameterIsNotNull(endDate, "endDate cannot be null");
        validateDatesAreNotMutuallyExclusive(startDate.atStartOfDay(), endDate.atStartOfDay(), "startDate was after endDate or vice versa");

        return Optional.ofNullable(this.goalRepository.findGoalByNameAndStartDateAndEndDate(name, startDate, endDate));
    }

    /**
     * Creates a new {@link Goal} from the given {@link Map} of data
     *
     * @param data {@link Map}
     * @return newly created {@link Goal}
     */
    public Goal createGoal(Map<String, Object> data) {

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for creating a Goal was null or empty");
        }

        try {
            Goal goal = this.goalRepository.save(applyChanges(new Goal(), data));

            //  set all other goals as inactive
            if (goal.getActive()) {
                this.goalRepository.resetGoals();
            }

            return goal;
        } catch (Exception e) {
            throw new EntityCreationException(String.format("A Goal entity could not be created : %s" , e.getMessage()));
        }
    }

    /**
     * Updates an existing {@link Goal} with the given {@link Map} of data. Update methods are designed to be idempotent.
     *
     * @param data {@link Map}
     * @return modified {@link Goal}
     */
    public Goal updateGoal(String name, LocalDate start, LocalDate end, Map<String, Object> data) {

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for updating the Goal was null or empty");
        }

        try {
            Goal goal =
                    findGoalForNameAndStartDateAndEndDate(name, start, end)
                            .orElseThrow(() -> new NoResultFoundException(String.format("No Goal found for name %s , start date %s and end date %s", name, start.format(DateTimeFormatter.ISO_DATE), end.format(DateTimeFormatter.ISO_DATE))));

            return applyChanges(goal, data);
        } catch (Exception e) {
            throw new EntityModificationException(String.format("An error occurred while modifying the Goal : %s" , e.getMessage()));
        }
    }


    //  HELPERS

    /**
     * Applies data changes to the given {@link Goal}
     *
     * @param goal {@link Goal} to change
     * @param data {@link Map} of data
     * @return {@link Goal}
     */
    private Goal applyChanges(final Goal goal, Map<String, Object> data) {
        goal.setStatus(GoalStatus.valueOf(data.get("status").toString().toUpperCase()));
        goal.setActive(Boolean.parseBoolean(data.get("active").toString()));
        goal.setName(data.get("name").toString());
        goal.setStartDate(LocalDate.parse(data.get("startDate").toString(), DateTimeFormatter.ISO_DATE));
        goal.setEndDate(LocalDate.parse(data.get("endDate").toString(), DateTimeFormatter.ISO_DATE));
        goal.setProfitTarget(BigDecimal.valueOf(Double.parseDouble(data.get("profitTarget").toString())).setScale(2, RoundingMode.HALF_EVEN).doubleValue());

        return this.goalRepository.save(goal);
    }
}
