package com.stephenprizio.traderbuddy.repositories.goals;

import com.stephenprizio.traderbuddy.enums.goals.GoalStatus;
import com.stephenprizio.traderbuddy.models.entities.goals.Goal;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for {@link Goal}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface GoalRepository extends CrudRepository<Goal, Long> {

    /**
     * Returns the currently active {@link Goal}. The application will only support 1 active {@link Goal} at a time
     * therefore whenever a {@link Goal} is set to active, all other goals will have their active flag set to false
     *
     * @return {@link Goal} with active = true
     */
    List<Goal> findGoalByActiveIsTrue();

    /**
     * Returns a {@link List} of {@link Goal}s by their {@link GoalStatus}
     *
     * @param status {@link GoalStatus}
     * @return {@link List} of {@link Goal}s
     */
    List<Goal> findAllByStatus(GoalStatus status);

    /**
     * Returns a {@link Goal} for the given name, start date and end date
     *
     * @param name goal name
     * @param startDate {@link LocalDate} start
     * @param endDate {@link LocalDate} end
     * @return {@link Goal}
     */
    Goal findGoalByNameAndStartDateAndEndDate(String name, LocalDate startDate, LocalDate endDate);

    /**
     * Will reset all active goals to inactive
     */
    @Modifying
    @Query("UPDATE Goal g SET g.active = false WHERE g.active = true")
    int resetGoals();
}
