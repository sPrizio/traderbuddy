package com.stephenprizio.traderbuddy.converters.plans;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.models.dto.plans.WithdrawalPlanDTO;
import com.stephenprizio.traderbuddy.services.platform.UniqueIdentifierService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link WithdrawalPlanDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class WithdrawalPlanDTOConverterTest extends AbstractGenericTest {

    @Autowired
    private WithdrawalPlanDTOConverter withdrawalPlanDTOConverter;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


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
