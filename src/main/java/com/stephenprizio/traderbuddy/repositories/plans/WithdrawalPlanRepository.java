package com.stephenprizio.traderbuddy.repositories.plans;

import com.stephenprizio.traderbuddy.models.entities.plans.WithdrawalPlan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link WithdrawalPlan}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface WithdrawalPlanRepository extends CrudRepository<WithdrawalPlan, Long> {
}
