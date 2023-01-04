package com.traderbuddyv2.api.controllers.trade.record;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.models.records.trade.MonthRecord;
import com.traderbuddyv2.core.models.records.trade.YearRecord;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import com.traderbuddyv2.core.services.trade.TradeService;
import com.traderbuddyv2.core.services.trade.record.TradeRecordService;
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

import java.time.Month;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link TradeRecordApiController}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class TradeRecordApiControllerTest extends AbstractGenericTest {

    @MockBean
    private MathService mathService;

    @MockBean
    private TradeService tradeService;

    @MockBean
    private TradeRecordService tradeRecordService;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        Mockito.when(this.mathService.getDouble(anyDouble())).thenReturn(0.0);
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.tradeRecordService.findRecentHistory(anyInt(), any())).thenReturn(List.of(generateTestTradeRecord()));
        Mockito.when(this.tradeRecordService.findHistory(any(), any(), any())).thenReturn(List.of(generateTestTradeRecord()));
        Mockito.when(this.tradeRecordService.findActiveMonths(anyInt())).thenReturn(List.of(new MonthRecord(Month.JANUARY, true, 10, 135.65)));
        Mockito.when(this.tradeRecordService.findActiveYears()).thenReturn(List.of(new YearRecord(2023, true, 10, 136.63)));
        Mockito.when(this.tradeService.findAllTradesWithinTimespan(any(), any(), anyBoolean())).thenReturn(List.of(generateTestBuyTrade(), generateTestSellTrade()));
    }


    //  ----------------- getRecentHistory -----------------

    @Test
    public void test_getRecentHistory_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("count", List.of("2"));
        map.put("aggregateInterval", List.of("BAD"));
        map.put("sortOrder", List.of("desc"));

        this.mockMvc.perform(get("/api/v1/trade-record/log").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(String.format(CoreConstants.Validation.INVALID_INTERVAL, "BAD"))));
    }

    @Test
    public void test_getRecentHistory_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("count", List.of("2"));
        map.put("aggregateInterval", List.of("DAILY"));
        map.put("sortOrder", List.of("asc"));

        this.mockMvc.perform(get("/api/v1/trade-record/log").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].startDate", containsString("2022-08-24")));
    }


    //  ----------------- getHistory -----------------

    @Test
    public void test_getHistory_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24"));
        map.put("end", List.of("2022-08-25"));
        map.put("aggregateInterval", List.of("BAD"));

        this.mockMvc.perform(get("/api/v1/trade-record/history").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Looks like your request could not be processed. Check your inputs and try again")));
    }

    @Test
    public void test_getHistory_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24"));
        map.put("end", List.of("2022-08-25"));
        map.put("aggregateInterval", List.of("DAILY"));

        this.mockMvc.perform(get("/api/v1/trade-record/history").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].startDate", containsString("2022-08-24")));
    }


    //  ----------------- getActiveMonths -----------------

    @Test
    public void test_getActiveMonths_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("year", List.of("2022"));

        this.mockMvc.perform(get("/api/v1/trade-record/active-months").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].netProfit", is(135.65)));
    }


    //  ----------------- getActiveYears -----------------

    @Test
    public void test_getActiveYears_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/trade-record/active-years"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].netProfit", is(136.63)));
    }
}
