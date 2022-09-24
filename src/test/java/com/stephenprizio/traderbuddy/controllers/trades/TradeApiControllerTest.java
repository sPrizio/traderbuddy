package com.stephenprizio.traderbuddy.controllers.trades;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.trades.TradeType;
import com.stephenprizio.traderbuddy.enums.trades.TradingPlatform;
import com.stephenprizio.traderbuddy.models.entities.trades.Trade;
import com.stephenprizio.traderbuddy.services.importing.impl.GenericImportService;
import com.stephenprizio.traderbuddy.services.platform.UniqueIdentifierService;
import com.stephenprizio.traderbuddy.services.trades.TradeService;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link TradeApiController}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class TradeApiControllerTest extends AbstractGenericTest {

    private final Trade TEST_TRADE_1 = generateTestBuyTrade();
    private final Trade TEST_TRADE_2 = generateTestSellTrade();

    private final MockMultipartFile TEST_FILE = new MockMultipartFile("file", "hello.csv", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
    private final MockMultipartFile TEST_FILE2 = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

    @MockBean
    private GenericImportService genericImportService;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Before
    public void setUp() {
        Mockito.when(this.genericImportService.importTrades(any(), anyChar(), any())).thenReturn(StringUtils.EMPTY);
        Mockito.when(this.genericImportService.importTrades(any(), eq('|'), eq(TradingPlatform.UNDEFINED))).thenReturn("Error Message");
        Mockito.when(this.tradeService.findAllByTradeType(TradeType.BUY, true)).thenReturn(List.of(TEST_TRADE_1));
        Mockito.when(this.tradeService.findAllTradesWithinDate(any(), any(), anyBoolean())).thenReturn(List.of(TEST_TRADE_1, TEST_TRADE_2));
        Mockito.when(this.tradeService.findTradeByTradeId("testId1")).thenReturn(Optional.of(TEST_TRADE_1));
        Mockito.when(this.tradeService.disregardTrade("testId1")).thenReturn(true);
        Mockito.when(this.tradeService.disregardTrade("badId")).thenReturn(false);
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- getTradesForTradeType -----------------

    @Test
    public void test_getTradesForTradeType_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("tradeType", List.of("BAD"));
        map.put("includeNonRelevant", List.of("true"));

        this.mockMvc.perform(get("/api/v1/trades/for-type").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("BAD is not a valid trade type")));
    }

    @Test
    public void test_getTradesForTradeType_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("tradeType", List.of("BUY"));
        map.put("includeNonRelevant", List.of("true"));

        this.mockMvc.perform(get("/api/v1/trades/for-type").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].openPrice", is(13083.41)))
                .andExpect(jsonPath("$.data[0].closePrice", is(13098.67)))
                .andExpect(jsonPath("$.data[0].netProfit", is(14.85)));
    }


    //  ----------------- getTradesWithinInterval -----------------

    @Test
    public void test_getTradesWithinInterval_missingParamStart() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("dasdfasdfaf"));
        map.put("end", List.of("2022-08-25T00:00:00"));
        map.put("includeNonRelevant", List.of("true"));

        this.mockMvc.perform(get("/api/v1/trades/for-interval").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The start date dasdfasdfaf was not of the expected format yyyy-MM-dd'T'HH:mm:ss")));
    }

    @Test
    public void test_getTradesWithinInterval_missingParamEnd() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-25T00:00:00"));
        map.put("end", List.of("asdadasdasd"));
        map.put("includeNonRelevant", List.of("true"));

        this.mockMvc.perform(get("/api/v1/trades/for-interval").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The end date asdadasdasd was not of the expected format yyyy-MM-dd'T'HH:mm:ss")));
    }

    @Test
    public void test_getTradesWithinInterval_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24T00:00:00"));
        map.put("end", List.of("2022-08-25T00:00:00"));
        map.put("includeNonRelevant", List.of("true"));

        this.mockMvc.perform(get("/api/v1/trades/for-interval").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].openPrice", is(13083.41)))
                .andExpect(jsonPath("$.data[0].closePrice", is(13098.67)))
                .andExpect(jsonPath("$.data[0].netProfit", is(14.85)));
    }


    //  ----------------- getTradeForTradeId -----------------

    @Test
    public void test_getTradeForTradeId_missingParamTradeId() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("tradeId", List.of("asdasdad"));

        this.mockMvc.perform(get("/api/v1/trades/for-trade-id").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("No trade was found with trade id: asdasdad")));
    }

    @Test
    public void test_getTradeForTradeId_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("tradeId", List.of("testId1"));

        this.mockMvc.perform(get("/api/v1/trades/for-trade-id").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.tradeId", is("testId1")));
    }


    //  ----------------- postImportTrades -----------------

    @Test
    public void test_postImportTrades_badParamPlatform() throws Exception {

        MockMvc mockMvc1 = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("delimiter", List.of(","));
        map.put("tradePlatform", List.of("BAD_PLATFORM"));

        mockMvc1.perform(MockMvcRequestBuilders.multipart("/api/v1/trades/import-trades").file(TEST_FILE).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("BAD_PLATFORM is not a valid trading platform or is not currently supported")));
    }

    @Test
    public void test_postImportTrades_badFileFormat() throws Exception {
        MockMvc mockMvc1 = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("delimiter", List.of("|"));
        map.put("tradePlatform", List.of("UNDEFINED"));

        mockMvc1.perform(MockMvcRequestBuilders.multipart("/api/v1/trades/import-trades").file(TEST_FILE2).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The given file hello.txt was not a csv file")));
    }

    @Test
    public void test_postImportTrades_errorDuringImport() throws Exception {
        MockMvc mockMvc1 = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("delimiter", List.of("|"));
        map.put("tradePlatform", List.of("UNDEFINED"));

        mockMvc1.perform(MockMvcRequestBuilders.multipart("/api/v1/trades/import-trades").file(TEST_FILE).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Error Message")));
    }

    @Test
    public void test_postImportTrades_success() throws Exception {
        MockMvc mockMvc1 = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("delimiter", List.of(","));
        map.put("tradePlatform", List.of("CMC_MARKETS"));

        mockMvc1.perform(MockMvcRequestBuilders.multipart("/api/v1/trades/import-trades").file(TEST_FILE).params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(true)));
    }


    //  ----------------- putDisregardTrade -----------------

    @Test
    public void test_putDisregardTrade_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(put("/api/v1/trades/disregard").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("json did not contain of the required keys : [tradeId]")));
    }

    @Test
    public void test_putDisregardTrade_badId() throws Exception {
        this.mockMvc.perform(put("/api/v1/trades/disregard").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("tradeId", "badId"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("An error occurred while updating trade : badId. Please try again")));
    }

    @Test
    public void test_putDisregardTrade_success() throws Exception {
        this.mockMvc.perform(put("/api/v1/trades/disregard").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("tradeId", "testId1"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(true)));
    }
}
