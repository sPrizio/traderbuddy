package com.stephenprizio.traderbuddy.integrations.client.eod;

import com.stephenprizio.traderbuddy.exceptions.validation.IllegalParameterException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link EODIntegrationClient}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EODIntegrationClientTest {

    @Resource(name = "eodIntegrationClient")
    private EODIntegrationClient eodIntegrationClient;


    //  ----------------- get -----------------

    @Test
    public void test_get_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.eodIntegrationClient.get(null, new LinkedMultiValueMap<>()))
                .withMessage("url cannot be null");
    }

    @Test
    public void test_get_success() {
        assertThat(this.eodIntegrationClient.get("https://eodhistoricaldata.com/api/intraday", new LinkedMultiValueMap<>()))
                .isNotEmpty();
    }
}
