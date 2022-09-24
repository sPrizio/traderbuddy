package com.stephenprizio.traderbuddy.converters.plans;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.models.dto.plans.DepositPlanDTO;
import com.stephenprizio.traderbuddy.services.platform.UniqueIdentifierService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link DepositPlanDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class DepositPlanDTOConverterTest extends AbstractGenericTest {

    @Autowired
    private DepositPlanDTOConverter depositPlanDTOConverter;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


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
