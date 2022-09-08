package com.stephenprizio.traderbuddy.converters.plans;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.enums.plans.TradingPlanStatus;
import com.stephenprizio.traderbuddy.models.dto.plans.TradingPlanDTO;
import com.stephenprizio.traderbuddy.models.dto.plans.WithdrawalPlanDTO;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link WithdrawalPlanDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class WithdrawalPlanDTOConverterTest extends AbstractGenericTest {

    private final WithdrawalPlanDTOConverter withdrawalPlanDTOConverter = new WithdrawalPlanDTOConverter();

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.withdrawalPlanDTOConverter.convert(null))
                .isNotNull()
                .satisfies(WithdrawalPlanDTO::isEmpty);

    }

    @Test
    public void test_convert_success() {
        assertThat(this.withdrawalPlanDTOConverter.convert(generateWithdrawalPlan()))
                .isNotNull()
                .extracting("amount")
                .isEqualTo(generateWithdrawalPlan().getAmount());

    }

    @Test
    public void test_convertAll_success() {
        assertThat(this.withdrawalPlanDTOConverter.convertAll(List.of(generateWithdrawalPlan())))
                .isNotEmpty()
                .first()
                .extracting("amount")
                .isEqualTo(generateWithdrawalPlan().getAmount());
    }
}
