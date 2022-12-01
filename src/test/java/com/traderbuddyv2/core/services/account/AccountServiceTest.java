package com.traderbuddyv2.core.services.account;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.plan.TradingPlanService;
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
    private MathService mathService;

    @MockBean
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @MockBean
    private TradeRecordService tradeRecordService;

    @MockBean
    private TradingPlanService tradingPlanService;

    @Autowired
    private AccountService accountService;

    @Before
    public void setUp() {
        Mockito.when(this.traderBuddyUserDetailsService.getCurrentUser()).thenReturn(generateTestUser());
        Mockito.when(this.tradeRecordService.findRecentHistory(anyInt(), any())).thenReturn(List.of(generateTestTradeRecord()));
        Mockito.when(this.tradingPlanService.findCurrentlyActiveTradingPlan()).thenReturn(Optional.of(generateTestTradingPlan()));
        Mockito.when(this.mathService.computeIncrement(anyDouble(), anyDouble(), anyBoolean())).thenReturn(100.0);
        Mockito.when(this.tradeRecordService.findHistory(any(), any(), any())).thenReturn(List.of(generateTestTradeRecord()));
        Mockito.when(this.mathService.add(anyDouble(), anyDouble())).thenReturn(100.0);
        Mockito.when(this.mathService.subtract(anyDouble(), anyDouble())).thenReturn(-100.0);
        Mockito.when(this.tradingPlanService.forecast(any(), any(), any(), any())).thenReturn(List.of());
    }


    //  ----------------- getAccountOverview -----------------

    @Test
    public void test_getAccountOverview_success() {
        assertThat(this.accountService.getAccountOverview())
                .isNotNull()
                .extracting("dateTime", "balance")
                .containsExactly(LocalDateTime.of(2022, 8, 24, 0, 0, 0), 1000.0);
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
}
