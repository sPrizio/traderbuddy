package com.stephenprizio.traderbuddy.services.importing.impl;

import com.stephenprizio.traderbuddy.enums.trades.TradingPlatform;
import com.stephenprizio.traderbuddy.exceptions.importing.TradeImportFailureException;
import com.stephenprizio.traderbuddy.exceptions.validation.IllegalParameterException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.InputStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link GenericImportService}
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
@RunWith(SpringRunner.class)
public class GenericImportServiceTest {

    private final MockMultipartFile TEST_FILE = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

    @MockBean
    private CMCTradesImportService cmcTradesImportService;

    private GenericImportService genericImportService;


    @Before
    public void setUp() throws Exception {
        this.genericImportService = new GenericImportService(this.cmcTradesImportService);
        Mockito.doNothing().when(this.cmcTradesImportService).importTrades(TEST_FILE.getInputStream(), ',');
        Mockito.doThrow(new TradeImportFailureException("Test Exception")).when(this.cmcTradesImportService).importTrades(TEST_FILE.getInputStream(), '|');
    }

    @Test
    public void test_importTrades_missingParamFile() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.genericImportService.importTrades(null, '|', TradingPlatform.CMC_MARKETS))
                .withMessage("import stream cannot be null");
    }

    @Test
    public void test_importTrades_missingParamDelimiter() throws Exception {
        InputStream inputStream = TEST_FILE.getInputStream();
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.genericImportService.importTrades(inputStream, null, TradingPlatform.CMC_MARKETS))
                .withMessage("delimiter cannot be null");
    }

    @Test
    public void test_importTrades_missingParamPlatform() throws Exception {
        InputStream inputStream = TEST_FILE.getInputStream();
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.genericImportService.importTrades(inputStream, '|', null))
                .withMessage("trading platform cannot be null");
    }

    @Test
    public void test_importTrades_failure_platformNotSupported() throws Exception {
        assertThat(this.genericImportService.importTrades(TEST_FILE.getInputStream(), ',', TradingPlatform.UNDEFINED))
                .isNotEmpty()
                .isEqualTo("Trading platform UNDEFINED is not currently supported");
    }

    @Test
    public void test_importTrades_success_cmc() throws Exception {
        assertThat(this.genericImportService.importTrades(TEST_FILE.getInputStream(), ',', TradingPlatform.CMC_MARKETS))
                .isEmpty();
    }
}
