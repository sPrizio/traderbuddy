package com.traderbuddyv2.core.repositories.retrospective;

import com.traderbuddyv2.core.models.entities.retrospective.AudioRetrospective;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link AudioRetrospective}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface AudioRetrospectiveRepository extends PagingAndSortingRepository<AudioRetrospective, Long> {
}
