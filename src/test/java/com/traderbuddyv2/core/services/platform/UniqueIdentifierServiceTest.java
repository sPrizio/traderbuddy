package com.traderbuddyv2.core.services.platform;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.models.dto.trade.TradeDTO;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import org.apache.commons.lang3.StringUtils;
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

    private final Trade mockedTrade = Mockito.mock(Trade.class, Mockito.RETURNS_DEEP_STUBS);

    @Before
    public void setUp() {
        Mockito.when(mockedTrade.getId()).thenReturn(118L);
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
        assertThat(this.uniqueIdentifierService.generateUid(this.mockedTrade))
                .isEqualTo("MTE4");
    }


    //  ----------------- retrieveId -----------------

    @Test
    public void test_retrieveId_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.uniqueIdentifierService.retrieveId(null))
                .withMessage(CoreConstants.Validation.UID_CANNOT_BE_NULL);

        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setUid(StringUtils.EMPTY);
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.uniqueIdentifierService.retrieveId(tradeDTO.getUid()))
                .withMessage("uid is missing");
    }

    @Test
    public void test_retrieveId_success() {
        TradeDTO tradeDTO = new TradeDTO();
        tradeDTO.setUid("MTE4");

        assertThat(this.uniqueIdentifierService.retrieveId(tradeDTO.getUid()))
                .isEqualTo(118L);
    }


    //  ----------------- retrieveIdForUid -----------------

    @Test
    public void test_retrieveIdForUid_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.uniqueIdentifierService.retrieveIdForUid(null))
                .withMessage(CoreConstants.Validation.UID_CANNOT_BE_NULL);
    }

    @Test
    public void test_retrieveIdForUid_success() {
        assertThat(this.uniqueIdentifierService.retrieveIdForUid("MTE4"))
                .isEqualTo(118L);
    }
}
