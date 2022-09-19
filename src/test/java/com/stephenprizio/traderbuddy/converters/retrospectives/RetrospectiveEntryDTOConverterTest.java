package com.stephenprizio.traderbuddy.converters.retrospectives;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.models.dto.retrospectives.RetrospectiveEntryDTO;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link RetrospectiveEntryDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class RetrospectiveEntryDTOConverterTest extends AbstractGenericTest {

    private final RetrospectiveEntryDTOConverter retrospectiveEntryDTOConverter = new RetrospectiveEntryDTOConverter();

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
