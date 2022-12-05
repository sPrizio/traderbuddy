package com.traderbuddyv2.core.services.account;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.plan.TradingPlan;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import com.traderbuddyv2.core.models.nonentities.AccountOverview;
import com.traderbuddyv2.core.models.records.account.EquityCurveEntry;
import com.traderbuddyv2.core.models.records.plan.ForecastEntry;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.plan.TradingPlanService;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import com.traderbuddyv2.core.services.trade.record.TradeRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.traderbuddyv2.core.validation.GenericValidator.validateDatesAreNotMutuallyExclusive;
import static com.traderbuddyv2.core.validation.GenericValidator.validateParameterIsNotNull;

/**
 * Service-layer for {@link Account}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@Component("accountService")
public class AccountService {

    @Resource(name = "mathService")
    private MathService mathService;

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

        final AccountOverview accountOverview = new AccountOverview();
        accountOverview.setBalance(account.getBalance());

        if (CollectionUtils.isNotEmpty(monthlyRecords)) {
            TradeRecord tradeRecord = monthlyRecords.get(0);
            accountOverview.setMonthlyEarnings(tradeRecord.getBalance());
            accountOverview.setDateTime(tradeRecord.getStartDate().atStartOfDay());
        }

        if (CollectionUtils.isNotEmpty(dailyRecords) && ChronoUnit.DAYS.between(dailyRecords.get(0).getStartDate(), LocalDate.now()) < 2) {
            accountOverview.setDailyEarnings(dailyRecords.get(0).getStatistics().getNetProfit());
        }

        plan.ifPresent(p -> accountOverview.setNextTarget(this.mathService.computeIncrement(account.getBalance(), p.getProfitTarget(), p.isAbsolute())));
        return accountOverview;
    }

    /**
     * Returns a {@link List} of {@link EquityCurveEntry}
     *
     * @param start {@link LocalDate}
     * @param end {@link LocalDate}
     * @param aggregateInterval {@link AggregateInterval}
     * @return {@link List} of {@link EquityCurveEntry}
     */
    public List<EquityCurveEntry> getEquityCurve(final LocalDate start, final LocalDate end, final AggregateInterval aggregateInterval) {

        validateParameterIsNotNull(start, CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(aggregateInterval, CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);
        validateDatesAreNotMutuallyExclusive(start.atStartOfDay(), end.atStartOfDay(), CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);

        final List<TradeRecord> tradeRecords = this.tradeRecordService.findHistory(start, end, aggregateInterval);
        final List<EquityCurveEntry> entries = new ArrayList<>();
        Optional<TradingPlan> activePlan = this.tradingPlanService.findCurrentlyActiveTradingPlan();

        Map<Pair<LocalDate, LocalDate>, ForecastEntry> entryMap = new HashMap<>();
        activePlan.ifPresent(tradingPlan -> this.tradingPlanService.forecast(tradingPlan, aggregateInterval, start, end).forEach(entry -> entryMap.put(Pair.of(entry.startDate(), entry.endDate()), entry)));

        double count = 0.0;
        for (TradeRecord tradeRecord : tradeRecords) {
            if (activePlan.isPresent() && tradeRecord.getStartDate().isEqual(activePlan.get().getStartDate())) {
                count = this.mathService.add(activePlan.get().getStartingBalance(), count);
            }

            if (entryMap.containsKey(Pair.of(tradeRecord.getStartDate(), tradeRecord.getEndDate()))) {
                count = this.mathService.add(entryMap.get(Pair.of(tradeRecord.getStartDate(), tradeRecord.getEndDate())).deposits(), count);
                count = this.mathService.subtract(count, entryMap.get(Pair.of(tradeRecord.getStartDate(), tradeRecord.getEndDate())).withdrawals());
            }

            entries.add(new EquityCurveEntry(tradeRecord.getStartDate(), this.mathService.add(count, tradeRecord.getBalance())));
            count = this.mathService.add(count, tradeRecord.getBalance());
        }

        return entries;
    }
}
