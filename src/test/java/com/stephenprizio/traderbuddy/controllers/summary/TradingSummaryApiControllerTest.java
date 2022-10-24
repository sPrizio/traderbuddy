package com.stephenprizio.traderbuddy.controllers.summary;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.constants.TraderBuddyConstants;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingSummary;
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

import java.time.Month;
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
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class TradingSummaryApiControllerTest extends AbstractGenericTest {

    private final TradingSummary TRADING_SUMMARY = generateTradingSummary();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradingSummaryService tradingSummaryService;

    @Before
    public void setUp() {
        Mockito.when(this.tradingSummaryService.getSummaryForTimeSpan(any(), any())).thenReturn(TRADING_SUMMARY.records().get(0));
        Mockito.when(this.tradingSummaryService.getReportOfSummariesForTimeSpan(any(), any(), any())).thenReturn(TRADING_SUMMARY);
        Mockito.when(this.tradingSummaryService.getStatisticsForMonthAndYear(Month.OCTOBER, 2022)).thenReturn(TRADING_SUMMARY.statistics());
    }


    //  ----------------- getSummaryForTimeSpan -----------------

    @Test
    public void test_getTradesWithinInterval_missingParamStart() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("dasdfasdfaf"));
        map.put("end", List.of("2022-08-25T00:00:00"));

        this.mockMvc.perform(get("/api/v1/trade-summary/time-span").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(TraderBuddyConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_getTradesWithinInterval_missingParamEnd() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-25T00:00:00"));
        map.put("end", List.of("asdadasdasd"));

        this.mockMvc.perform(get("/api/v1/trade-summary/time-span").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(TraderBuddyConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_getTradesWithinInterval_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24T00:00:00"));
        map.put("end", List.of("2022-08-25T00:00:00"));

        this.mockMvc.perform(get("/api/v1/trade-summary/time-span").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.target", is(47.52)))
                .andExpect(jsonPath("$.data.totalNumberOfTrades", is(2)))
                .andExpect(jsonPath("$.data.netProfit", is(10.35)));
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
                .andExpect(jsonPath("$.message", containsString(TraderBuddyConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_getReportOfSummariesForTimeSpan_missingParamEnd() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-25T00:00:00"));
        map.put("end", List.of("asdadasdasd"));
        map.put("interval", List.of("DAILY"));

        this.mockMvc.perform(get("/api/v1/trade-summary/report").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(TraderBuddyConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
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
                .andExpect(jsonPath("$.data.records[0].totalNumberOfTrades", is(2)))
                .andExpect(jsonPath("$.data.records[0].netProfit", is(10.35)));
    }


    //  ----------------- getStatisticsForMonthAndYear -----------------

    @Test
    public void test_getStatisticsForMonthAndYear_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("month", List.of("BAD"));
        map.put("year", List.of("2022"));

        this.mockMvc.perform(get("/api/v1/trade-summary/monthly-stats").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("BAD is not a valid month")));
    }

    @Test
    public void test_getStatisticsForMonthAndYear_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("month", List.of("October"));
        map.put("year", List.of("2022"));

        this.mockMvc.perform(get("/api/v1/trade-summary/monthly-stats").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.netProfit", is(10.35)));
    }
}
