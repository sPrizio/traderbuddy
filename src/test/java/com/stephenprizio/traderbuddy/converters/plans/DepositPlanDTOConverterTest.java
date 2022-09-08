package com.stephenprizio.traderbuddy.converters.plans;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.models.dto.plans.DepositPlanDTO;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link DepositPlanDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class DepositPlanDTOConverterTest extends AbstractGenericTest {

    private final DepositPlanDTOConverter depositPlanDTOConverter = new DepositPlanDTOConverter();

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.depositPlanDTOConverter.convert(null))
                .isNotNull()
                .satisfies(DepositPlanDTO::isEmpty);

    }

    @Test
    public void test_convert_success() {
        assertThat(this.depositPlanDTOConverter.convert(generateDepositPlan()))
                .isNotNull()
                .extracting("amount")
                .isEqualTo(generateDepositPlan().getAmount());

    }

    @Test
    public void test_convertAll_success() {
        assertThat(this.depositPlanDTOConverter.convertAll(List.of(generateDepositPlan())))
                .isNotEmpty()
                .first()
                .extracting("amount")
                .isEqualTo(generateDepositPlan().getAmount());
    }
}
