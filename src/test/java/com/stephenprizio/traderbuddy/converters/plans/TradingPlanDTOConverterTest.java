package com.stephenprizio.traderbuddy.converters.plans;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.enums.plans.TradingPlanStatus;
import com.stephenprizio.traderbuddy.models.dto.plans.TradingPlanDTO;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link TradingPlanDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradingPlanDTOConverterTest extends AbstractGenericTest {

    private final TradingPlanDTOConverter tradingPlanDTOConverter = new TradingPlanDTOConverter();

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
                .extracting("active", "name", "startDate", "endDate", "profitTarget", "status", "compoundFrequency", "startingBalance")
                .containsExactly(
                        true,
                        "Test Trading Plan Active",
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2025, 1, 1),
                        528491.0,
                        TradingPlanStatus.IN_PROGRESS,
                        CompoundFrequency.DAILY,
                        1000.0
                );

    }

    @Test
    public void test_convertAll_success() {
        assertThat(this.tradingPlanDTOConverter.convertAll(List.of(generateTestTradingPlan())))
                .isNotEmpty()
                .first()
                .extracting("active", "name", "startDate", "endDate", "profitTarget", "status", "compoundFrequency", "startingBalance")
                .containsExactly(
                        true,
                        "Test Trading Plan Active",
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2025, 1, 1),
                        528491.0,
                        TradingPlanStatus.IN_PROGRESS,
                        CompoundFrequency.DAILY,
                        1000.0
                );
    }
}
