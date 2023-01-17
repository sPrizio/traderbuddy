package com.traderbuddyv2.api.facades;

import com.traderbuddyv2.api.converters.account.AccountDTOConverter;
import com.traderbuddyv2.api.models.records.AccountOverview;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.plan.TradingPlan;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import com.traderbuddyv2.core.services.levelling.rank.RankService;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.plan.TradingPlanService;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import com.traderbuddyv2.core.services.trade.record.TradeRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Facade-layer for {@link Account}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("accountFacade")
public class AccountFacade {

    @Resource(name = "accountDTOConverter")
    private AccountDTOConverter accountDTOConverter;

    @Resource(name = "mathService")
    private MathService mathService;

    @Resource(name = "rankService")
    private RankService rankService;

    @Resource(name = "tradeRecordService")
    private TradeRecordService tradeRecordService;

    @Resource(name = "traderBuddyUserDetailsService")
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @Resource(name = "tradingPlanService")
    private TradingPlanService tradingPlanService;


    //  METHODS

    /**
     * Returns an {@link AccountOverview} for the current user
     *
     * @return {@link AccountOverview}
     */
    public AccountOverview getAccountOverview() {

        final Account account = this.traderBuddyUserDetailsService.getCurrentUser().getAccount();
        final List<TradeRecord> monthlyRecords = this.tradeRecordService.findRecentHistory(1, AggregateInterval.MONTHLY);
        final List<TradeRecord> dailyRecords = this.tradeRecordService.findRecentHistory(1, AggregateInterval.DAILY);
        final Optional<TradingPlan> plan = this.tradingPlanService.findCurrentlyActiveTradingPlan();

        double monthlyEarnings = 0.0;
        double dailyEarnings = 0.0;
        LocalDateTime localDateTime = null;

        if (CollectionUtils.isNotEmpty(monthlyRecords)) {
            TradeRecord tradeRecord = monthlyRecords.get(0);
            monthlyEarnings = tradeRecord.getBalance();
            localDateTime = tradeRecord.getStartDate().atStartOfDay();
        }

        if (CollectionUtils.isNotEmpty(dailyRecords)) {
            dailyEarnings = dailyRecords.get(0).getStatistics().getNetProfit();
        }

        return
                new AccountOverview(
                        localDateTime,
                        account.getBalance(),
                        monthlyEarnings,
                        dailyEarnings,
                        plan.map(p -> this.mathService.computeIncrement(account.getBalance(), p.getProfitTarget(), p.isAbsolute())).orElse(0.0),
                        this.accountDTOConverter.convert(account),
                        this.rankService.getCurrentRank());
    }
}
