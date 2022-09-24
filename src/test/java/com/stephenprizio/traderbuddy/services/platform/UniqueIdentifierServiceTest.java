package com.stephenprizio.traderbuddy.services.platform;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.exceptions.validation.IllegalParameterException;
import com.stephenprizio.traderbuddy.models.dto.plans.DepositPlanDTO;
import com.stephenprizio.traderbuddy.models.entities.plans.DepositPlan;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class {@link UniqueIdentifierService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class UniqueIdentifierServiceTest extends AbstractGenericTest {

    private final UniqueIdentifierService uniqueIdentifierService = new UniqueIdentifierService();

    private final DepositPlan mockedPlan = Mockito.mock(DepositPlan.class, Mockito.RETURNS_DEEP_STUBS);

    @Before
    public void setUp() {
        Mockito.when(mockedPlan.getId()).thenReturn(118L);
    }


    //  ----------------- generateUid -----------------

    @Test
    public void test_generateUid_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.uniqueIdentifierService.generateUid(null))
                .withMessage("entity cannot be null");
    }

    @Test
    public void test_generateUid_success() {
        assertThat(this.uniqueIdentifierService.generateUid(this.mockedPlan))
                .isEqualTo("MTE4");
    }


    //  ----------------- retrieveId -----------------

    @Test
    public void test_retrieveId_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.uniqueIdentifierService.retrieveId(null))
                .withMessage("dto cannot be null");

        DepositPlanDTO depositPlanDTO = new DepositPlanDTO();
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.uniqueIdentifierService.retrieveId(depositPlanDTO))
                .withMessage("dto's uid is missing");
    }

    @Test
    public void test_retrieveId_success() {
        DepositPlanDTO depositPlanDTO = generateDepositPlanDTO();
        depositPlanDTO.setUid("MTE4");

        assertThat(this.uniqueIdentifierService.retrieveId(depositPlanDTO))
                .isEqualTo(118L);
    }
}
