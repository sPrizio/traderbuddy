package com.stephenprizio.traderbuddy.repositories.plans;

import com.stephenprizio.traderbuddy.models.entities.plans.DepositPlan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link DepositPlan}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface DepositPlanRepository extends CrudRepository<DepositPlan, Long> {
}
