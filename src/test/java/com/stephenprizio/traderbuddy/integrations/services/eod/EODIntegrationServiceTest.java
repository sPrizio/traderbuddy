package com.stephenprizio.traderbuddy.integrations.services.eod;

import com.stephenprizio.traderbuddy.exceptions.validation.IllegalParameterException;
import com.stephenprizio.traderbuddy.integrations.client.eod.EODIntegrationClient;
import com.stephenprizio.traderbuddy.integrations.exceptions.IntegrationException;
import com.stephenprizio.traderbuddy.integrations.translators.eod.IntradayHistoricalDataTranslator;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link EODIntegrationService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class EODIntegrationServiceTest {

    private final MultiValueMap<String, String> goodMap = new LinkedMultiValueMap<>();
    private final MultiValueMap<String, String> badMap = new LinkedMultiValueMap<>();

    @Mock
    private EODIntegrationClient eodIntegrationClient;

    @Mock
    private IntradayHistoricalDataTranslator intradayHistoricalDataTranslator = new IntradayHistoricalDataTranslator();

    @InjectMocks
    private EODIntegrationService eodIntegrationService = new EODIntegrationService();

    @Before
    public void setUp() {
        ReflectionTestUtils.setField(eodIntegrationService, "intradayUrl", "https://eodhistoricaldata.com/api/intraday");

        goodMap.put("interval", List.of("1m"));
        goodMap.put("from", List.of("1564752900"));
        goodMap.put("to", List.of("1564753200"));
        Mockito.when(this.eodIntegrationClient.get("https://eodhistoricaldata.com/api/intraday/AAPL.US", this.goodMap)).thenReturn(
                """
                        [
                            {
                                "timestamp":1564752900,
                                "gmtoffset":0,
                                "datetime":"2019-08-02 13:35:00",
                                "open":205.14,
                                "high":205.37,
                                "low":204.75,
                                "close":204.7683,
                                "volume":231517
                            }
                        ]
                """
        );

        badMap.put("interval", List.of("5m"));
        badMap.put("from", List.of("-31557014135596800"));
        badMap.put("to", List.of("31556889832780799"));
        Mockito.when(this.eodIntegrationClient.get("bad", this.badMap)).thenReturn(StringUtils.EMPTY);
    }


    //  ----------------- getIntradayData -----------------

    @Test
    public void test_getIntradayData_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.eodIntegrationService.getIntradayData(null, "5m", LocalDateTime.MIN, LocalDateTime.MAX))
                .withMessage("symbol cannot be null");
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.eodIntegrationService.getIntradayData("empty", null, LocalDateTime.MIN, LocalDateTime.MAX))
                .withMessage("interval");
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.eodIntegrationService.getIntradayData("empty", "5m", null, LocalDateTime.MAX))
                .withMessage("from cannot be null");
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.eodIntegrationService.getIntradayData("empty", "5m", LocalDateTime.MIN, null))
                .withMessage("to cannot be null");
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.eodIntegrationService.getIntradayData("empty", "5m", LocalDateTime.MAX, LocalDateTime.MIN))
                .withMessage("from was greater than to or vice versa");
    }

    @Test
    public void test_getIntradayData_failure() {
        assertThatExceptionOfType(IntegrationException.class)
                .isThrownBy(() -> this.eodIntegrationService.getIntradayData("bad", "5m", LocalDateTime.MIN, LocalDateTime.MAX))
                .withMessage("An error occurred while connecting to https://eodhistoricaldata.com/api/intraday");
    }

    @Test
    public void test_getIntradayData_success() {
        assertThat(this.eodIntegrationService.getIntradayData("AAPL.US", "1m", LocalDateTime.of(2019, 8, 2, 13, 35, 0), LocalDateTime.of(2019, 8, 2, 13, 40, 0)))
                .isNotNull();
    }
}
