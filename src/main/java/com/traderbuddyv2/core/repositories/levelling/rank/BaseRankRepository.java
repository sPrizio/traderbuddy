package com.traderbuddyv2.core.repositories.levelling.rank;

import com.traderbuddyv2.core.models.entities.levelling.rank.BaseRank;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

/**
 * Data-access layer for {@link BaseRank}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Repository
public interface BaseRankRepository extends PagingAndSortingRepository<BaseRank, Long> {
}
