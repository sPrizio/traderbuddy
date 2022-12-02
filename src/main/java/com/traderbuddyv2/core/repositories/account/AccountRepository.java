package com.traderbuddyv2.core.repositories.account;

import com.traderbuddyv2.core.models.entities.account.Account;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Account}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
}
