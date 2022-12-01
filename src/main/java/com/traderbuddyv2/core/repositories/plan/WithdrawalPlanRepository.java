package com.traderbuddyv2.core.repositories.plan;

import com.traderbuddyv2.core.models.entities.plan.WithdrawalPlan;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link WithdrawalPlan}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface WithdrawalPlanRepository extends PagingAndSortingRepository<WithdrawalPlan, Long> {
}
