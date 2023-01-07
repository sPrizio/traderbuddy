package com.traderbuddyv2.core.services.levelling.rank;

import com.traderbuddyv2.core.models.entities.levelling.rank.BaseRank;
import com.traderbuddyv2.core.models.entities.levelling.rank.Rank;
import com.traderbuddyv2.core.repositories.levelling.rank.BaseRankRepository;
import com.traderbuddyv2.core.repositories.levelling.rank.RankRepository;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Service-layer for {@link BaseRank} & {@link Rank}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("rankService")
public class RankService {

    @Resource(name = "baseRankRepository")
    private BaseRankRepository baseRankRepository;

    @Resource(name = "rankRepository")
    private RankRepository rankRepository;


    //  METHODS

    /**
     * Obtains all of the {@link BaseRank}as a sorted set
     *
     * @return {@link Set} of {@link BaseRank}
     */
    public List<BaseRank> getAllBaseRanks() {
        Set<BaseRank> baseRanks = new TreeSet<>();
        this.baseRankRepository.findAll().forEach(baseRanks::add);
        return new ArrayList<>(baseRanks);
    }
}
