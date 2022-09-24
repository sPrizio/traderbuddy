package com.stephenprizio.traderbuddy.converters.retrospectives;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.models.dto.retrospectives.RetrospectiveEntryDTO;
import com.stephenprizio.traderbuddy.services.platform.UniqueIdentifierService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link RetrospectiveEntryDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RetrospectiveEntryDTOConverterTest extends AbstractGenericTest {

    @Autowired
    private RetrospectiveEntryDTOConverter retrospectiveEntryDTOConverter;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.retrospectiveEntryDTOConverter.convert(null))
                .isNotNull()
                .satisfies(RetrospectiveEntryDTO::isEmpty);

    }

    @Test
    public void test_convert_success() {
        assertThat(this.retrospectiveEntryDTOConverter.convert(generateEntries().get(0)))
                .isNotNull()
                .extracting("lineNumber", "entryText", "keyPoint")
                .containsExactly(
                        1,
                        "Test 1",
                        true
                );
    }

    @Test
    public void test_convertAll_success() {
        assertThat(this.retrospectiveEntryDTOConverter.convertAll(generateEntries()))
                .isNotEmpty()
                .first()
                .extracting("lineNumber", "entryText", "keyPoint")
                .containsExactly(
                        1,
                        "Test 1",
                        true
                );
    }
}
