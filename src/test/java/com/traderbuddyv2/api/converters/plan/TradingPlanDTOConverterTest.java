package com.traderbuddyv2.api.converters.plan;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.models.dto.plans.TradingPlanDTO;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.enums.plans.TradingPlanStatus;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link TradingPlanDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TradingPlanDTOConverterTest extends AbstractGenericTest {

    @MockBean
    private DepositPlanDTOConverter depositPlanDTOConverter;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @MockBean
    private WithdrawalPlanDTOConverter withdrawalPlanDTOConverter;

    @Autowired
    private TradingPlanDTOConverter tradingPlanDTOConverter;


    @Before
    public void setUp() {
        Mockito.when(this.depositPlanDTOConverter.convert(any())).thenReturn(generateDepositPlanDTO());
        Mockito.when(this.withdrawalPlanDTOConverter.convert(any())).thenReturn(generateWithdrawalPlanDTO());
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.tradingPlanDTOConverter.convert(null))
                .isNotNull()
                .satisfies(TradingPlanDTO::isEmpty);

    }

    @Test
    public void test_convert_success() {
        assertThat(this.tradingPlanDTOConverter.convert(generateTestTradingPlan()))
                .isNotNull()
                .extracting("active", "name", "startDate", "endDate", "profitTarget", "status", "aggregateInterval", "startingBalance", "depositPlan.amount", "withdrawalPlan.amount")
                .containsExactly(
                        true,
                        "Test Trading Plan Active",
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2025, 1, 1),
                        1.25,
                        TradingPlanStatus.IN_PROGRESS,
                        AggregateInterval.DAILY,
                        1000.0,
                        generateDepositPlan().getAmount(),
                        generateWithdrawalPlan().getAmount()
                );

    }


    //  ----------------- convertAll -----------------

    @Test
    public void test_convertAll_success() {
        assertThat(this.tradingPlanDTOConverter.convertAll(List.of(generateTestTradingPlan())))
                .isNotEmpty()
                .first()
                .extracting("active", "name", "startDate", "endDate", "profitTarget", "status", "aggregateInterval", "startingBalance", "depositPlan.amount", "withdrawalPlan.amount")
                .containsExactly(
                        true,
                        "Test Trading Plan Active",
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2025, 1, 1),
                        1.25,
                        TradingPlanStatus.IN_PROGRESS,
                        AggregateInterval.DAILY,
                        1000.0,
                        generateDepositPlan().getAmount(),
                        generateWithdrawalPlan().getAmount()
                );
    }
}
