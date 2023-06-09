package com.traderbuddyv2.importing.services;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import com.traderbuddyv2.importing.exceptions.TradeImportFailureException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
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
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class GenericImportServiceTest extends AbstractGenericTest {

    private final MockMultipartFile TEST_FILE = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

    @MockBean
    private CMCMarketsTradesImportService cmcMarketsTradesImportService;

    @MockBean
    private MetaTrader4TradesImportService metaTrader4TradesImportService;

    @MockBean
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    private GenericImportService genericImportService;


    @Before
    public void setUp() throws Exception {
        this.genericImportService = new GenericImportService(this.cmcMarketsTradesImportService, metaTrader4TradesImportService, traderBuddyUserDetailsService);
        Mockito.when(this.traderBuddyUserDetailsService.getCurrentUser()).thenReturn(generateTestUser());
        Mockito.doNothing().when(this.cmcMarketsTradesImportService).importTrades(TEST_FILE.getInputStream(), ',');
        Mockito.doThrow(new TradeImportFailureException("Test Exception")).when(this.cmcMarketsTradesImportService).importTrades(TEST_FILE.getInputStream(), '|');
    }

    @Test
    public void test_importTrades_missingParamFile() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.genericImportService.importTrades(null))
                .withMessage("import stream cannot be null");
    }

    @Test
    public void test_importTrades_success_cmc() throws Exception {
        assertThat(this.genericImportService.importTrades(TEST_FILE.getInputStream()))
                .isEmpty();
    }
}
