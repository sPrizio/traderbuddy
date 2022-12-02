package com.traderbuddyv2.core.repositories.account;

import com.traderbuddyv2.core.models.entities.account.AccountBalanceModification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link AccountBalanceModification}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface AccountBalanceModificationRepository extends PagingAndSortingRepository<AccountBalanceModification, Long> {
}
