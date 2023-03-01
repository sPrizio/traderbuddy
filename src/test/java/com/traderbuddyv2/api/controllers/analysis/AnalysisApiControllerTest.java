package com.traderbuddyv2.api.controllers.analysis;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.models.nonentities.analysis.bucket.TradeTimeBucket;
import com.traderbuddyv2.core.models.nonentities.analysis.performance.TradePerformance;
import com.traderbuddyv2.core.models.nonentities.analysis.performance.TradeRecordPerformanceBucket;
import com.traderbuddyv2.core.models.nonentities.analysis.performance.TradeRecordPerformanceBucketStatistics;
import com.traderbuddyv2.core.models.nonentities.analysis.performance.TradeRecordPerformanceBucketWrapper;
import com.traderbuddyv2.core.models.nonentities.trade.IrrelevantTradeTotals;
import com.traderbuddyv2.core.services.analysis.AnalysisService;
import org.hamcrest.Matchers;
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

import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link AnalysisApiController}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class AnalysisApiControllerTest extends AbstractGenericTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalysisService analysisService;

    @Before
    public void setUp() {
        Mockito.when(this.analysisService.getTopTradePerformance(any(), any(), any(), anyBoolean(), anyInt())).thenReturn(List.of(new TradePerformance(generateTestBuyTrade())));
        Mockito.when(this.analysisService.getAverageTradePerformance(any(), any(), anyBoolean(), anyInt())).thenReturn(generateAverageTradePerformance());
        Mockito.when(this.analysisService.getTradeBuckets(any(), any(), any())).thenReturn(List.of(new TradeTimeBucket(LocalTime.MIN, LocalTime.MAX, List.of())));
        Mockito.when(this.analysisService.getWinningDaysBreakdown(any(), any(), anyInt(), anyBoolean())).thenReturn(new TradeRecordPerformanceBucketWrapper(List.of(new TradeRecordPerformanceBucket(50, 100, 17)), new TradeRecordPerformanceBucketStatistics(0.0, 0)));
        Mockito.when(this.analysisService.getIrrelevantTrades(any(), any())).thenReturn(new IrrelevantTradeTotals(List.of(generateTestBuyTrade(), generateTestSellTrade()), List.of()));
        Mockito.when(this.analysisService.getTradeDayBuckets(any(), any())).thenReturn(Map.of());
    }


    //  ----------------- getTopTrades -----------------

    @Test
    public void test_getTopTrades_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-01-01"));
        map.put("end", List.of("2022-02-01"));
        map.put("sort", List.of("BAD"));
        map.put("sortByLosses", List.of("false"));
        map.put("count", List.of("1"));

        this.mockMvc.perform(get("/api/v1/analysis/top-trades").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("BAD is not a valid sort")));
    }

    @Test
    public void test_getTopTrades_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-01-01"));
        map.put("end", List.of("2022-02-01"));
        map.put("sort", List.of("PIPS"));
        map.put("sortByLosses", List.of("false"));
        map.put("count", List.of("1"));

        this.mockMvc.perform(get("/api/v1/analysis/top-trades").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].pips", Matchers.is(15.26)));
    }


    //  ----------------- getAverage -----------------

    @Test
    public void test_getAverage_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-01-01"));
        map.put("end", List.of("2022-02-01"));
        map.put("win", List.of("false"));
        map.put("count", List.of("1"));

        this.mockMvc.perform(get("/api/v1/analysis/average").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.profitability", Matchers.is(1.89)));
    }


    //  ----------------- getTradeBuckets -----------------

    @Test
    public void test_getTradeBuckets_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-01-01"));
        map.put("end", List.of("2022-02-01"));
        map.put("bucket", List.of("5m"));

        this.mockMvc.perform(get("/api/v1/analysis/bucket").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].start", Matchers.is("00:00:00")));
    }


    //  ----------------- getWinningTradeBuckets -----------------

    @Test
    public void test_getWinningTradeBuckets_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-01-01"));
        map.put("end", List.of("2022-02-01"));
        map.put("bucketSize", List.of("50"));
        map.put("isLoser", List.of("false"));

        this.mockMvc.perform(get("/api/v1/analysis/winning-buckets").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.buckets[0].count", Matchers.is(17)));
    }


    //  ----------------- getIrrelevantTrades -----------------

    @Test
    public void test_getIrrelevantTrades_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-01-01"));
        map.put("end", List.of("2022-02-01"));

        this.mockMvc.perform(get("/api/v1/analysis/irrelevant-trades").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.current", Matchers.notNullValue()));
    }


    //  ----------------- getWeekdayPerformance -----------------

    @Test
    public void test_getWeekdayPerformance_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-01-01"));
        map.put("end", List.of("2022-02-01"));

        this.mockMvc.perform(get("/api/v1/analysis/weekday-performance").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", Matchers.notNullValue()));
    }
}
