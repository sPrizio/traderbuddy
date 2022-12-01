package com.traderbuddyv2.core.repositories.retrospective;

import com.traderbuddyv2.core.models.entities.retrospective.RetrospectiveEntry;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link RetrospectiveEntry}s
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface RetrospectiveEntryRepository extends CrudRepository<RetrospectiveEntry, Long> {
}
