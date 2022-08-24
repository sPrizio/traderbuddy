package com.stephenprizio.traderbuddy.controllers;

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

import java.time.LocalDateTime;
import java.util.List;

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
public class TradeApiControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @Before
    public void setUp() {

        Trade trade1 = new Trade();
        Trade trade2 = new Trade();

        trade1.setResultOfTrade("Winner winner chicken dinner");
        trade1.setTradeType(TradeType.BUY);
        trade1.setClosePrice(13098.67);
        trade1.setTradeCloseTime(LocalDateTime.of(2022, 8, 24, 11, 37, 24));
        trade1.setTradeOpenTime(LocalDateTime.of(2022, 8, 24, 11, 32, 58));
        trade1.setLotSize(0.75);
        trade1.setNetProfit(14.85);
        trade1.setOpenPrice(13083.41);
        trade1.setReasonForEntrance("I have my reasons");

        trade2.setResultOfTrade("Loser like a real loser");
        trade2.setTradeType(TradeType.SELL);
        trade2.setClosePrice(13156.12);
        trade2.setTradeCloseTime(LocalDateTime.of(2022, 8, 24, 10, 24, 36));
        trade2.setTradeOpenTime(LocalDateTime.of(2022, 8, 24, 10, 25, 12));
        trade2.setLotSize(0.75);
        trade2.setNetProfit(-4.50);
        trade2.setOpenPrice(13160.09);
        trade2.setReasonForEntrance("I continue to have my reasons");

        Mockito.when(this.tradeService.findAllByTradeType(TradeType.BUY)).thenReturn(List.of(trade1));
        Mockito.when(this.tradeService.findAllTradesWithinDate(any(), any())).thenReturn(List.of(trade1, trade2));
    }


    //  ----------------- getTradesForTradeType -----------------

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
                .andExpect(status().isBadRequest());
    }

    @Test
    public void test_getTradesWithinInterval_missingParamEnd() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-25T00:00:00"));
        map.put("end", List.of("asdadasdasd"));

        this.mockMvc.perform(get("/api/v1/trades/for-interval").params(map))
                .andExpect(status().isBadRequest());
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
}
