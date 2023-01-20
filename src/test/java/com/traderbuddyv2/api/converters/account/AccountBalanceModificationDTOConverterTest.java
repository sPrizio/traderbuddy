package com.traderbuddyv2.api.converters.account;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.models.dto.account.AccountBalanceModificationDTO;
import com.traderbuddyv2.api.models.dto.account.AccountDTO;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
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
 * Testing class for {@link AccountBalanceModificationDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class AccountBalanceModificationDTOConverterTest extends AbstractGenericTest {

    @Autowired
    private AccountBalanceModificationDTOConverter accountBalanceModificationDTOConverter;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.accountBalanceModificationDTOConverter.convert(null))
                .isNotNull()
                .satisfies(AccountBalanceModificationDTO::isEmpty);

    }

    @Test
    public void test_convert_success() {
        assertThat(this.accountBalanceModificationDTOConverter.convert(generateTestAccountBalanceModification()))
                .isNotNull()
                .extracting("amount", "modificationType")
                .containsExactly(350.0, "One-time Deposit");

    }


    //  ----------------- convertAll -----------------

    @Test
    public void test_convertAll_success() {
        assertThat(this.accountBalanceModificationDTOConverter.convertAll(List.of(generateTestAccountBalanceModification())))
                .isNotEmpty()
                .first()
                .extracting("amount", "modificationType")
                .containsExactly(350.0, "One-time Deposit");
    }
}
