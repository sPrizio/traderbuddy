package com.stephenprizio.traderbuddy.integrations.translators.eod;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link IntradayHistoricalDataTranslator}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class IntradayHistoricalDataTranslatorTest extends AbstractGenericTest {

    @Resource(name = "intradayHistoricalDataTranslator")
    private final IntradayHistoricalDataTranslator intradayHistoricalDataTranslator = new IntradayHistoricalDataTranslator();


    //  ----------------- translate -----------------

    @Test
    public void test_translate_empty() {
        assertThat(this.intradayHistoricalDataTranslator.translate(null))
                .isNull();
    }

    @Test
    public void test_translate_success() {
        assertThat(this.intradayHistoricalDataTranslator.translate(generateIntradayResponse()))
                .isNotNull();
    }
}
