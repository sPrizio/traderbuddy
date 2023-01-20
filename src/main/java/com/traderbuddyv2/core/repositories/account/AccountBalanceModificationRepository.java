package com.traderbuddyv2.core.repositories.account;

import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.account.AccountBalanceModification;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository for {@link AccountBalanceModification}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface AccountBalanceModificationRepository extends PagingAndSortingRepository<AccountBalanceModification, Long> {

    /**
     * Returns a {@link List} of {@link AccountBalanceModification} that match the processed flag for the given {@link Account}
     *
     * @param processed process flag
     * @param account {@link Account}
     * @return {@link List} of {@link AccountBalanceModification}
     */
    List<AccountBalanceModification> findAllByProcessedAndAccount(final boolean processed, final Account account);
}
