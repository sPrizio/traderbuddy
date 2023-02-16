package com.traderbuddyv2.api.converters.security;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.models.dto.security.UserLocaleDTO;
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
 * Testing class for {@link UserLocaleDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserLocaleDTOConverterTest extends AbstractGenericTest {

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Autowired
    private UserLocaleDTOConverter userLocaleDTOConverter;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.userLocaleDTOConverter.convert(null))
                .isNotNull()
                .satisfies(UserLocaleDTO::isEmpty);

    }

    @Test
    public void test_convert_success() {
        assertThat(this.userLocaleDTOConverter.convert(generateTestUserLocale()))
                .isNotNull()
                .extracting("townCity", "country")
                .containsExactly("Montreal", "Canada");

    }


    //  ----------------- convertAll -----------------

    @Test
    public void test_convertAll_success() {
        assertThat(this.userLocaleDTOConverter.convertAll(List.of(generateTestUserLocale())))
                .isNotEmpty()
                .first()
                .extracting("townCity", "country")
                .containsExactly("Montreal", "Canada");
    }
}
