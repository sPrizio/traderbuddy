package com.traderbuddyv2.core.repositories.plan;

import com.traderbuddyv2.core.enums.plans.TradingPlanStatus;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.plan.TradingPlan;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
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
public interface TradingPlanRepository extends PagingAndSortingRepository<TradingPlan, Long> {

    /**
     * Returns the currently active {@link TradingPlan}. The application will only support 1 active {@link TradingPlan} at a time
     * therefore whenever a {@link TradingPlan} is set to active, all other plans will have their active flag set to false
     *
     * @param account {@link Account}
     * @return {@link TradingPlan} with active = true
     */
    TradingPlan findTopTradingPlanByActiveIsTrueAndAccountOrderByStartDateDesc(final Account account);

    /**
     * Returns a {@link List} of {@link TradingPlan}s by their {@link TradingPlanStatus}
     *
     * @param status {@link TradingPlanStatus}
     * @param account {@link Account}
     * @return {@link List} of {@link TradingPlan}s
     */
    List<TradingPlan> findAllByStatusAndAccount(final TradingPlanStatus status, final Account account);

    /**
     * Returns a {@link TradingPlan} for the given name, start date and end date
     *
     * @param name      TradingPlan name
     * @param startDate {@link LocalDate} start
     * @param endDate   {@link LocalDate} end
     * @return {@link TradingPlan}
     */
    TradingPlan findTradingPlanByNameAndStartDateAndEndDateAndAccount(final String name, final LocalDate startDate, final LocalDate endDate, final Account account);

    /**
     * Will reset all active TradingPlans to inactive
     */
    @Modifying
    @Query("UPDATE TradingPlan g SET g.active = false WHERE g.active = true AND g.account = ?1")
    int resetTradingPlans(final Account account);
}
