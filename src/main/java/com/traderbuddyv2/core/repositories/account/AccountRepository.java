package com.traderbuddyv2.core.repositories.account;

import com.traderbuddyv2.core.models.entities.account.Account;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * Repository for {@link Account}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public interface AccountRepository extends PagingAndSortingRepository<Account, Long> {
}
