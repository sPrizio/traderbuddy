package com.stephenprizio.traderbuddy.controllers.summary;

import com.stephenprizio.traderbuddy.AbstractTraderBuddyTest;
import com.stephenprizio.traderbuddy.models.records.reporting.TradingSummary;
import com.stephenprizio.traderbuddy.services.summary.TradingSummaryService;
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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link TradingSummaryApiController}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class TradingSummaryApiControllerTest extends AbstractTraderBuddyTest {

    private final TradingSummary TRADING_SUMMARY = generateTradingSummary();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradingSummaryService tradingSummaryService;

    @Before
    public void setUp() {
        Mockito.when(this.tradingSummaryService.getSummaryForTimeSpan(any(), any())).thenReturn(TRADING_SUMMARY.records().get(0));
        Mockito.when(this.tradingSummaryService.getReportOfSummariesForTimeSpan(any(), any(), any())).thenReturn(TRADING_SUMMARY);
    }


    //  ----------------- getSummaryForTimeSpan -----------------

    @Test
    public void test_getTradesWithinInterval_missingParamStart() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("dasdfasdfaf"));
        map.put("end", List.of("2022-08-25T00:00:00"));

        this.mockMvc.perform(get("/api/v1/trade-summary/time-span").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The start date dasdfasdfaf was not of the expected format yyyy-MM-dd'T'HH:mm:ss")));
    }

    @Test
    public void test_getTradesWithinInterval_missingParamEnd() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-25T00:00:00"));
        map.put("end", List.of("asdadasdasd"));

        this.mockMvc.perform(get("/api/v1/trade-summary/time-span").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The end date asdadasdasd was not of the expected format yyyy-MM-dd'T'HH:mm:ss")));
    }

    @Test
    public void test_getTradesWithinInterval_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24T00:00:00"));
        map.put("end", List.of("2022-08-25T00:00:00"));

        this.mockMvc.perform(get("/api/v1/trade-summary/time-span").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.target", is(47.52)))
                .andExpect(jsonPath("$.data.numberOfTrades", is(15)))
                .andExpect(jsonPath("$.data.netProfit", is(58.63)));
    }


    //  ----------------- getReportOfSummariesForTimeSpan -----------------

    @Test
    public void test_getReportOfSummariesForTimeSpan_missingParamStart() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("dasdfasdfaf"));
        map.put("end", List.of("2022-08-25T00:00:00"));
        map.put("interval", List.of("DAILY"));

        this.mockMvc.perform(get("/api/v1/trade-summary/report").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The start date dasdfasdfaf was not of the expected format yyyy-MM-dd'T'HH:mm:ss")));
    }

    @Test
    public void test_getReportOfSummariesForTimeSpan_missingParamEnd() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-25T00:00:00"));
        map.put("end", List.of("asdadasdasd"));
        map.put("interval", List.of("DAILY"));

        this.mockMvc.perform(get("/api/v1/trade-summary/report").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The end date asdadasdasd was not of the expected format yyyy-MM-dd'T'HH:mm:ss")));
    }

    @Test
    public void test_getReportOfSummariesForTimeSpan_missingParamInterval() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-25T00:00:00"));
        map.put("end", List.of("2022-08-25T00:00:00"));
        map.put("interval", List.of("NAD"));

        this.mockMvc.perform(get("/api/v1/trade-summary/report").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("NAD is not a valid time interval")));
    }

    @Test
    public void test_getReportOfSummariesForTimeSpan_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24T00:00:00"));
        map.put("end", List.of("2022-08-25T00:00:00"));
        map.put("interval", List.of("DAILY"));

        this.mockMvc.perform(get("/api/v1/trade-summary/report").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.records[0].target", is(47.52)))
                .andExpect(jsonPath("$.data.records[0].numberOfTrades", is(15)))
                .andExpect(jsonPath("$.data.records[0].netProfit", is(58.63)));
    }
}
