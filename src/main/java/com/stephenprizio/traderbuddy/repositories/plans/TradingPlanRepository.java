package com.stephenprizio.traderbuddy.repositories.plans;

import com.stephenprizio.traderbuddy.enums.plans.TradingPlanStatus;
import com.stephenprizio.traderbuddy.models.entities.plans.TradingPlan;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository for {@link TradingPlan}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface TradingPlanRepository extends CrudRepository<TradingPlan, Long> {

    /**
     * Returns the currently active {@link TradingPlan}. The application will only support 1 active {@link TradingPlan} at a time
     * therefore whenever a {@link TradingPlan} is set to active, all other plans will have their active flag set to false
     *
     * @return {@link TradingPlan} with active = true
     */
    List<TradingPlan> findTradingPlanByActiveIsTrue();

    /**
     * Returns a {@link List} of {@link TradingPlan}s by their {@link TradingPlanStatus}
     *
     * @param status {@link TradingPlanStatus}
     * @return {@link List} of {@link TradingPlan}s
     */
    List<TradingPlan> findAllByStatus(TradingPlanStatus status);

    /**
     * Returns a {@link TradingPlan} for the given name, start date and end date
     *
     * @param name      TradingPlan name
     * @param startDate {@link LocalDate} start
     * @param endDate   {@link LocalDate} end
     * @return {@link TradingPlan}
     */
    TradingPlan findTradingPlanByNameAndStartDateAndEndDate(String name, LocalDate startDate, LocalDate endDate);

    /**
     * Will reset all active TradingPlans to inactive
     */
    @Modifying
    @Query("UPDATE TradingPlan g SET g.active = false WHERE g.active = true")
    int resetTradingPlans();
}
