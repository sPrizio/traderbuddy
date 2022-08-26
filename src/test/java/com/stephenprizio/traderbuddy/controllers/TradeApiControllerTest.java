package com.stephenprizio.traderbuddy.controllers;

import com.stephenprizio.traderbuddy.AbstractTraderBuddyTest;
import com.stephenprizio.traderbuddy.enums.TradeType;
import com.stephenprizio.traderbuddy.models.entities.Trade;
import com.stephenprizio.traderbuddy.services.TradeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
public class TradeApiControllerTest extends AbstractTraderBuddyTest {

    private final Trade TEST_TRADE_1 = generateTestBuyTrade();
    private final Trade TEST_TRADE_2 = generateTestSellTrade();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @Before
    public void setUp() {
        Mockito.when(this.tradeService.findAllByTradeType(TradeType.BUY)).thenReturn(List.of(TEST_TRADE_1));
        Mockito.when(this.tradeService.findAllTradesWithinDate(any(), any())).thenReturn(List.of(TEST_TRADE_1, TEST_TRADE_2));
        Mockito.when(this.tradeService.findTradeByTradeId("testId1")).thenReturn(Optional.of(TEST_TRADE_1));
    }


    //  ----------------- getTradesForTradeType -----------------

    @Test
    public void test_getTradesForTradeType_badRequest() throws Exception {
        this.mockMvc.perform(get("/api/v1/trades/for-type?tradeType=BAD"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_getTradesForTradeType_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/trades/for-type?tradeType=BUY"))
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

        this.mockMvc.perform(get("/api/v1/trades/for-interval").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The start date dasdfasdfaf was not of the expected format yyyy-MM-dd'T'HH:mm:ss")));
    }

    @Test
    public void test_getTradesWithinInterval_missingParamEnd() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-25T00:00:00"));
        map.put("end", List.of("asdadasdasd"));

        this.mockMvc.perform(get("/api/v1/trades/for-interval").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The end date asdadasdasd was not of the expected format yyyy-MM-dd'T'HH:mm:ss")));
    }

    @Test
    public void test_getTradesWithinInterval_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24T00:00:00"));
        map.put("end", List.of("2022-08-25T00:00:00"));

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
}
