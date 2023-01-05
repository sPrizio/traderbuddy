package com.traderbuddyv2.api.facades;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.converters.levelling.skill.SkillDTOConverter;
import com.traderbuddyv2.api.models.dto.levelling.skill.SkillDTO;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.*;

/**
 * Testing class for {@link AccountFacade}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountFacadeTest extends AbstractGenericTest {

    @MockBean
    private MathService mathService;

    @MockBean
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @MockBean
    private TradeRecordService tradeRecordService;

    @MockBean
    private TradingPlanService tradingPlanService;

    @MockBean
    private SkillDTOConverter skillDTOConverter;

    @Autowired
    private AccountFacade accountFacade;

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
        Mockito.when(this.skillDTOConverter.convert(any())).thenReturn(new SkillDTO());
    }

    //  ----------------- getAccountOverview -----------------

    @Test
    public void test_getAccountOverview_success() {
        assertThat(this.accountFacade.getAccountOverview())
                .isNotNull()
                .extracting("dateTime", "balance")
                .containsExactly(LocalDateTime.of(2022, 8, 24, 0, 0, 0), 1000.0);
    }
}
