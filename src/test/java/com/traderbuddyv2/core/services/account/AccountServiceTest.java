package com.traderbuddyv2.core.services.account;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.account.AccountBalanceModificationType;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.exceptions.system.EntityCreationException;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import com.traderbuddyv2.core.exceptions.validation.MissingRequiredDataException;
import com.traderbuddyv2.core.repositories.account.AccountBalanceModificationRepository;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import com.traderbuddyv2.core.services.trade.record.TradeRecordService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.*;

/**
 * Testing class for {@link AccountService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountServiceTest extends AbstractGenericTest {

    @MockBean
    private TradeRecordService tradeRecordService;

    @MockBean
    private AccountBalanceModificationRepository accountBalanceModificationRepository;

    @MockBean
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Autowired
    private AccountService accountService;

    @Before
    public void setUp() {
        Mockito.when(this.tradeRecordService.findRecentHistory(anyInt(), any())).thenReturn(List.of(generateTestTradeRecord()));
        Mockito.when(this.tradeRecordService.findHistory(any(), any(), any())).thenReturn(List.of(generateTestTradeRecord()));
        Mockito.when(this.accountBalanceModificationRepository.findAllByProcessedAndAccountOrderByDateTimeDesc(anyBoolean(), any())).thenReturn(List.of(generateTestAccountBalanceModification()));
        Mockito.when(this.traderBuddyUserDetailsService.getCurrentUser()).thenReturn(generateTestUser());
        Mockito.when(this.uniqueIdentifierService.retrieveIdForUid("test")).thenReturn(1L);
        Mockito.when(this.accountBalanceModificationRepository.save(any())).thenReturn(generateTestAccountBalanceModification());
        Mockito.when(this.accountBalanceModificationRepository.findById(1L)).thenReturn(Optional.of(generateTestAccountBalanceModification()));
    }


    //  ----------------- getEquityCurve -----------------

    @Test
    public void test_getEquityCurve_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.getEquityCurve(null, LocalDate.MAX, AggregateInterval.MONTHLY))
                .withMessage(CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.getEquityCurve(LocalDate.MIN, null, AggregateInterval.MONTHLY))
                .withMessage(CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.getEquityCurve(LocalDate.MIN, LocalDate.MAX, null))
                .withMessage(CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.accountService.getEquityCurve(LocalDate.MAX, LocalDate.MIN, AggregateInterval.MONTHLY))
                .withMessage(CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    public void test_getEquityCurve_success() {
        assertThat(this.accountService.getEquityCurve(LocalDate.MIN, LocalDate.MAX, AggregateInterval.DAILY))
                .isNotEmpty();
    }


    //  ----------------- findAccountBalanceHistory -----------------

    @Test
    public void test_findAccountBalanceHistory_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.findAccountBalanceHistory(null, LocalDate.MAX))
                .withMessage(CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.findAccountBalanceHistory(LocalDate.MIN, null))
                .withMessage(CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    public void test_findAccountBalanceHistory_success() {
        assertThat(this.accountService.findAccountBalanceHistory(LocalDate.MIN, LocalDate.MAX))
                .isNotEmpty()
                .first()
                .extracting("amount", "modificationType")
                .containsExactly(350.0, AccountBalanceModificationType.ONE_TIME_DEPOSIT);
    }


    //  ----------------- findAccountBalanceModificationForUid -----------------

    @Test
    public void test_findAccountBalanceModificationForUid_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.findAccountBalanceModificationForUid(null))
                .withMessage(CoreConstants.Validation.UID_CANNOT_BE_NULL);
    }

    @Test
    public void test_findAccountBalanceModificationForUid_success() {
        assertThat(this.accountService.findAccountBalanceModificationForUid("test"))
                .isNotEmpty();
    }


    //  ----------------- createAccountBalanceModification -----------------

    @Test
    public void test_createAccountBalanceModification_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.accountService.createAccountBalanceModification(null))
                .withMessage("The required data for creating an AccountBalanceModification was null or empty");
    }

    @Test
    public void test_createAccountBalanceModification_erroneousCreation() {
        Map<String, Object> map = Map.of("bad", "input");
        assertThatExceptionOfType(EntityCreationException.class)
                .isThrownBy(() -> this.accountService.createAccountBalanceModification(map))
                .withMessage("An AccountBalanceModification could not be created : Cannot invoke \"java.util.Map.get(Object)\" because \"mod\" is null");
    }

    @Test
    public void test_createAccountBalanceModification_success() {

        Map<String, Object> data =
                Map.of(
                        "modification",
                        Map.of(
                                "dateTime", "2022-09-05T11:08:53",
                                "amount", "123.45",
                                "type", "0",
                                "description", "Test"
                        )
                );

        assertThat(this.accountService.createAccountBalanceModification(data))
                .isNotNull()
                .extracting("dateTime", "amount", "modificationType")
                .containsExactly(LocalDateTime.of(2022, 9, 12, 1, 1, 1), 350.0, AccountBalanceModificationType.getForOrdinal(0));
    }


    //  ----------------- deleteAccountBalanceModification -----------------

    @Test
    public void test_deleteAccountBalanceModification_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.accountService.deleteAccountBalanceModification(null))
                .withMessage(CoreConstants.Validation.UID_CANNOT_BE_NULL);
    }

    @Test
    public void test_deleteAccountBalanceModification_unknown_success() {
        assertThat(this.accountService.deleteAccountBalanceModification("unknown"))
                .isFalse();
    }

    @Test
    public void test_deleteAccountBalanceModification_success() {
        assertThat(this.accountService.deleteAccountBalanceModification("test"))
                .isTrue();
    }
}
