package com.traderbuddyv2.core.repositories.levelling.rank;

import com.traderbuddyv2.core.models.entities.levelling.rank.Rank;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link Rank}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface RankRepository extends PagingAndSortingRepository<Rank, Long> {
}
