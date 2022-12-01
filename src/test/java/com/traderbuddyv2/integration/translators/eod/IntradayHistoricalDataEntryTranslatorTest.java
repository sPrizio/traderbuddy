package com.traderbuddyv2.integration.translators.eod;

import com.traderbuddyv2.AbstractGenericTest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link IntradayHistoricalDataEntryTranslator}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class IntradayHistoricalDataEntryTranslatorTest extends AbstractGenericTest {

    private final IntradayHistoricalDataEntryTranslator intradayHistoricalDataEntryTranslator = new IntradayHistoricalDataEntryTranslator();


    //  ----------------- translate -----------------

    @Test
    public void test_translate_empty() {
        assertThat(this.intradayHistoricalDataEntryTranslator.translate(null))
                .isNull();
    }

    @Test
    public void test_translate_success() {
        assertThat(this.intradayHistoricalDataEntryTranslator.translate(generateIntradayResponse().entries().get(0)))
                .isNotNull();
    }
}
