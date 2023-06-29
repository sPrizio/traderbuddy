package com.traderbuddyv2.core.repositories.system;

import com.traderbuddyv2.core.models.entities.system.PhoneNumber;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link PhoneNumber}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface PhoneNumberRepository extends PagingAndSortingRepository<PhoneNumber, Long> {
}
