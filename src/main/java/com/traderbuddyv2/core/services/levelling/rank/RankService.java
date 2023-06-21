package com.traderbuddyv2.core.services.levelling.rank;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.levelling.rank.BaseRank;
import com.traderbuddyv2.core.models.entities.levelling.rank.Rank;
import com.traderbuddyv2.core.models.entities.plan.TradingPlan;
import com.traderbuddyv2.core.models.records.rank.CurrentRank;
import com.traderbuddyv2.core.models.records.rank.RankInterval;
import com.traderbuddyv2.core.repositories.account.AccountRepository;
import com.traderbuddyv2.core.repositories.levelling.rank.BaseRankRepository;
import com.traderbuddyv2.core.repositories.levelling.rank.RankRepository;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.plan.TradingPlanService;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Stream;

import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link BaseRank} & {@link Rank}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("rankService")
public class RankService {

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "baseRankRepository")
    private BaseRankRepository baseRankRepository;

    @Resource(name = "mathService")
    private MathService mathService;

    @Resource(name = "rankRepository")
    private RankRepository rankRepository;

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
        return new ArrayList<>(baseRanks).subList(1, baseRanks.size());
    }

    /**
     * Obtains an {@link Account}'s current rank
     *
     * @return {@link CurrentRank}
     */
    public CurrentRank getCurrentRank() {

        final Account account = this.traderBuddyUserDetailsService.getCurrentUser().getAccount();
        final List<RankInterval> intervals = this.tradingPlanService.findCurrentlyActiveTradingPlan().map(this::computeRankIntervals).orElse(Collections.emptyList());

        final List<CurrentRank> currentRanks =
                intervals
                        .stream()
                        .map(ri -> new CurrentRank(ri.getUid(), ri.name(), ri.value(), this.mathService.getInteger(account.getBalance()), this.mathService.getInteger(this.mathService.add(ri.value(), ri.increment())), ri.imageUrl(), ri.className(), null, null))
                        .toList();

        int index = 0;
        for (int i = 0; i < currentRanks.size(); i++) {
            if (currentRanks.get(i).start() <= account.getBalance() && currentRanks.get(i).end() > account.getBalance()) {
                index = i;
            }
        }

        if (currentRanks.isEmpty()) {
            return new CurrentRank(-1L, "Empty", 0, 0, 0, StringUtils.EMPTY, StringUtils.EMPTY, null, null);
        }

        final CurrentRank finalRank;
        final CurrentRank temp = currentRanks.get(index);

        if (index == 0) {
            finalRank = new CurrentRank(temp.uid(), temp.name(), temp.start(), temp.current(), temp.end(), temp.imageUrl(), temp.className(), null, currentRanks.get(index + 1));
        } else if (index == currentRanks.size() - 1) {
            finalRank = new CurrentRank(temp.uid(), temp.name(), temp.start(), temp.current(), temp.end(), temp.imageUrl(), temp.className(), currentRanks.get(index - 1), null);
        } else {
            finalRank = new CurrentRank(temp.uid(), temp.name(), temp.start(), temp.current(), temp.end(), temp.imageUrl(), temp.className(), currentRanks.get(index - 1), currentRanks.get(index + 1));
        }

        return finalRank;
    }

    /**
     * Computes the current {@link Rank} for the given {@link Account}
     *
     * @param account {@link Account}
     */
    public void computeRank(final Account account) {
        validateParameterIsNotNull(account, CoreConstants.Validation.ACCOUNT_CANNOT_BE_NULL);
        Optional<Rank> rank = this.rankRepository.findById(getCurrentRank().uid());
        rank.ifPresent(account::setRank);
        this.accountRepository.save(account);
    }

    /**
     * Returns the starter {@link Rank}
     *
     * @return {@link Rank}
     */
    public Rank getStarterRank() {

        final List<BaseRank> baseRanks = getAllBaseRanks();
        return
                baseRanks
                        .stream()
                        .filter(br -> br.getPriority() == 1)
                        .findFirst()
                        .map(BaseRank::getRanks)
                        .stream()
                        .flatMap(Collection::stream)
                        .filter(r -> r.getLevel() == 5)
                        .findFirst()
                        .orElseThrow(() -> new UnsupportedOperationException("Impossible Error"));
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
        rankIntervals.add(new RankInterval(-1L, "No Rank", 0, 0, StringUtils.EMPTY, StringUtils.EMPTY));

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

                left.getRanks().forEach(r -> rankIntervals.add(new RankInterval(r.getId(), left.getName() + " " + r.getLevel(), computeRankBound(increment, r, starter), this.mathService.getInteger(increment), r.getImageUrl(), r.getBaseRank().getName().toLowerCase())));
            }
        }

        final BaseRank lastRank = baseRanks.get(baseRanks.size() - 1);
        rankIntervals.add(new RankInterval(lastRank.getRanks().get(0).getId(), lastRank.getName() + " 1", this.mathService.getInteger(this.mathService.multiply(tradingPlan.getStartingBalance(), lastRank.getMultiplier())), 0, lastRank.getRanks().get(0).getImageUrl(), lastRank.getName().toLowerCase()));

        return rankIntervals;
    }

    /**
     * Computes the rank's bound, minimum value to be considered this rank
     *
     * @param increment level increment
     * @param rank      {@link Rank}
     * @param starter   starting value
     * @return rank level
     */
    private int computeRankBound(final double increment, final Rank rank, final int starter) {

        if (rank.getLevel() == 5) {
            return starter;
        }

        return this.mathService.getInteger(this.mathService.add(this.mathService.multiply(increment, Math.abs(this.mathService.subtract(rank.getLevel(), rank.getBaseRank().getRanks().size()))), starter));
    }
}
