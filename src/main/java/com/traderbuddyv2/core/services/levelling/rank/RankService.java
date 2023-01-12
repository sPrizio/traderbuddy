package com.traderbuddyv2.core.services.levelling.rank;

import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.levelling.rank.BaseRank;
import com.traderbuddyv2.core.models.entities.levelling.rank.Rank;
import com.traderbuddyv2.core.models.entities.plan.TradingPlan;
import com.traderbuddyv2.core.models.records.rank.CurrentRank;
import com.traderbuddyv2.core.models.records.rank.RankInterval;
import com.traderbuddyv2.core.repositories.levelling.rank.BaseRankRepository;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.plan.TradingPlanService;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;

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

    @Resource(name = "mathService")
    private MathService mathService;

    @Resource(name = "tradingPlanService")
    private TradingPlanService tradingPlanService;

    @Resource(name = "traderBuddyUserDetailsService")
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;


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

    /**
     * Obtains an {@link Account}'s current rank
     *
     * @return {@link CurrentRank}
     */
    public CurrentRank getCurrentRank() {

        final Account account = this.traderBuddyUserDetailsService.getCurrentUser().getAccount();
        final List<RankInterval> intervals = this.tradingPlanService.findCurrentlyActiveTradingPlan().map(this::computeRankIntervals).orElse(Collections.emptyList());

        return intervals
                        .stream()
                        .map(ri -> new CurrentRank(ri.name(), ri.value(), this.mathService.getInteger(account.getBalance()), this.mathService.getInteger(this.mathService.add(ri.value(), ri.increment())), account.getRank().getImageUrl()))
                        .filter(cr -> cr.start() <= account.getBalance())
                        .filter(cr -> cr.end() > account.getBalance())
                        .findFirst()
                        .orElse(null);
    }


    //  HELPERS

    /**
     * Returns a {@link List} of {@link RankInterval} for the given {@link TradingPlan}
     *
     * @param tradingPlan {@link TradingPlan}
     * @return {@link List} of {@link RankInterval}
     */
    private List<RankInterval> computeRankIntervals(final TradingPlan tradingPlan) {

        validateParameterIsNotNull(tradingPlan, "trading plan cannot be null");

        final List<RankInterval> rankIntervals = new ArrayList<>();
        rankIntervals.add(new RankInterval("No Rank", 0, 0));

        final List<BaseRank> baseRanks = getAllBaseRanks();
        for (int i = 0; i < baseRanks.size() - 1; i++) {
            if (baseRanks.get(i).getMultiplier() != 0) {
                final BaseRank left = baseRanks.get(i);
                final BaseRank right = baseRanks.get(i + 1);
                final int starter = this.mathService.getInteger(this.mathService.multiply(left.getMultiplier(), tradingPlan.getStartingBalance()));

                final double increment = this.mathService.divide(
                        this.mathService.multiply(
                                this.mathService.subtract(right.getMultiplier(), left.getMultiplier()),
                                tradingPlan.getStartingBalance()
                        ),
                        left.getRanks().size()
                );

                left.getRanks().forEach(r -> rankIntervals.add(new RankInterval(left.getName() + " " + r.getLevel(), computeRankBound(increment, r, starter), this.mathService.getInteger(increment))));
            }
        }

        final BaseRank lastRank = baseRanks.get(baseRanks.size() - 1);
        rankIntervals.add(new RankInterval(lastRank.getName() + " 1", this.mathService.getInteger(this.mathService.multiply(tradingPlan.getStartingBalance(), lastRank.getMultiplier())), 0));

        return rankIntervals;
    }

    /**
     * Computes the rank's bound, minimum value to be considered this rank
     *
     * @param increment level increment
     * @param rank {@link Rank}
     * @param starter starting value
     * @return rank level
     */
    private int computeRankBound(final double increment, final Rank rank, final int starter) {

        if (rank.getLevel() == 5) {
            return starter;
        }

        return this.mathService.getInteger(this.mathService.add(this.mathService.multiply(increment, Math.abs(this.mathService.subtract(rank.getLevel(), rank.getBaseRank().getRanks().size()))), starter));
    }
}
