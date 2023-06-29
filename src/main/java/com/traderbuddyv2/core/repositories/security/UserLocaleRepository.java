package com.traderbuddyv2.core.repositories.security;

import com.traderbuddyv2.core.models.entities.security.UserLocale;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link UserLocale}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface UserLocaleRepository extends PagingAndSortingRepository<UserLocale, Long> {
}
