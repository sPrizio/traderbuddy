package com.traderbuddyv2.core.services.account;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.account.AccountBalanceModificationType;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.enums.trades.TradeType;
import com.traderbuddyv2.core.exceptions.system.EntityCreationException;
import com.traderbuddyv2.core.exceptions.validation.MissingRequiredDataException;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.account.AccountBalanceModification;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import com.traderbuddyv2.core.models.records.account.EquityCurveEntry;
import com.traderbuddyv2.core.repositories.account.AccountBalanceModificationRepository;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import com.traderbuddyv2.core.services.trade.TradeService;
import com.traderbuddyv2.core.services.trade.record.TradeRecordService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    @Resource(name = "accountBalanceModificationRepository")
    private AccountBalanceModificationRepository accountBalanceModificationRepository;

    @Resource(name = "tradeRecordService")
    private TradeRecordService tradeRecordService;

    @Resource(name = "tradeService")
    private TradeService tradeService;

    @Resource(name = "traderBuddyUserDetailsService")
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @Resource(name = "uniqueIdentifierService")
    private UniqueIdentifierService uniqueIdentifierService;


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

    /**
     * Returns a {@link List} of {@link AccountBalanceModification} that are processed for the given {@link Account}
     *
     * @param start {@link LocalDate}
     * @param end {@link LocalDate}
     * @return {@link List} of {@link AccountBalanceModification}
     */
    public List<AccountBalanceModification> findAccountBalanceHistory(final LocalDate start, final LocalDate end) {

        validateParameterIsNotNull(start, CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        validateParameterIsNotNull(end, CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);

        final List<AccountBalanceModification> modifications = this.accountBalanceModificationRepository.findAllByAccountOrderByDateTimeDesc(this.traderBuddyUserDetailsService.getCurrentUser().getAccount());
        return
                modifications
                        .stream()
                        .filter(mod -> mod.getDateTime().isEqual(start.atStartOfDay()) || mod.getDateTime().isAfter(start.atStartOfDay()))
                        .filter(mod -> mod.getDateTime().isBefore(end.atStartOfDay()))
                        .toList();

    }

    /**
     * Returns an {@link AccountBalanceModification} for the given uid
     *
     * @param uid uid
     * @return {@link Optional} {@link AccountBalanceModification}
     */
    public Optional<AccountBalanceModification> findAccountBalanceModificationForUid(final String uid) {
        validateParameterIsNotNull(uid, CoreConstants.Validation.UID_CANNOT_BE_NULL);
        return this.accountBalanceModificationRepository.findById(this.uniqueIdentifierService.retrieveIdForUid(uid));
    }

    /**
     * Creates a new {@link AccountBalanceModification} from the given {@link Map} of data
     *
     * @param data {@link Map}
     * @return newly created {@link AccountBalanceModification}
     */
    public AccountBalanceModification createAccountBalanceModification(final Map<String, Object> data) {

        if (MapUtils.isEmpty(data)) {
            throw new MissingRequiredDataException("The required data for creating an AccountBalanceModification was null or empty");
        }

        try {
            return applyChanges(new AccountBalanceModification(), data);
        } catch (Exception e) {
            throw new EntityCreationException(String.format("An AccountBalanceModification could not be created : %s", e.getMessage()), e);
        }
    }

    /**
     * Deletes the {@link AccountBalanceModification} for the given uid
     *
     * @param uid uid
     * @return true if deleted, false if not
     */
    public boolean deleteAccountBalanceModification(final String uid) {

        validateParameterIsNotNull(uid, CoreConstants.Validation.UID_CANNOT_BE_NULL);
        Optional<AccountBalanceModification> modification = findAccountBalanceModificationForUid(uid);
        if (modification.isPresent()) {
            modification.get().setAccount(null);
            this.accountBalanceModificationRepository.save(modification.get());
            this.accountBalanceModificationRepository.delete(modification.get());
            return true;
        }

        return false;
    }

    /**
     * Returns a {@link List} of {@link Trade}s that are promotional payments {@link TradeType}
     *
     * @return {@link List} of {@link Trade}s
     */
    public List<Trade> getPromoPayments() {
        return this.tradeService.findAllByTradeType(TradeType.PROMOTIONAL_PAYMENT, true).stream().sorted(Comparator.comparing(Trade::getTradeOpenTime).reversed()).toList();
    }


    //  HELPERS

    /**
     * Applies changes to the given {@link AccountBalanceModification} with the given data
     *
     * @param modification {@link AccountBalanceModification}
     * @param data {@link Map}
     * @return updated {@link AccountBalanceModification}
     */
    private AccountBalanceModification applyChanges(AccountBalanceModification modification, final Map<String, Object> data) {

        Map<String, Object> mod = (Map<String, Object>) data.get("modification");

        modification.setDateTime(LocalDate.parse(mod.get("dateTime").toString(), DateTimeFormatter.ofPattern(CoreConstants.DATE_FORMAT)).atStartOfDay().plusSeconds(1));
        modification.setAmount(Double.parseDouble(mod.get("amount").toString()));
        modification.setModificationType(AccountBalanceModificationType.getForOrdinal(Integer.parseInt(mod.get("type").toString())));
        modification.setDescription(mod.get("description").toString());
        modification.setProcessed(false);
        modification.setAccount(this.traderBuddyUserDetailsService.getCurrentUser().getAccount());

        return this.accountBalanceModificationRepository.save(modification);
    }
}
