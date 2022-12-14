package com.traderbuddyv2.core.services.account;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import com.traderbuddyv2.core.models.records.account.EquityCurveEntry;
import com.traderbuddyv2.core.services.trade.record.TradeRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

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

    @Resource(name = "tradeRecordService")
    private TradeRecordService tradeRecordService;


    //  METHODS

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
        if (CollectionUtils.isNotEmpty(tradeRecords)) {
            return tradeRecords.stream().map(rec -> new EquityCurveEntry(rec.getStartDate(), rec.getBalance())).toList();
        }

        return Collections.emptyList();
    }
}
