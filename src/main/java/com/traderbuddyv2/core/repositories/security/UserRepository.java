package com.traderbuddyv2.core.repositories.security;

import com.traderbuddyv2.core.models.entities.security.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link User}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long> {

    /**
     * Attempts to look up a {@link User} by their username
     *
     * @param username username
     * @return {@link User}, can return null
     */
    User findUserByUsername(final String username);

    /**
     * Attempts to look up a {@link User} by their email
     *
     * @param email email
     * @return {@link User}, can return null
     */
    User findUserByEmail(final String email);
}
