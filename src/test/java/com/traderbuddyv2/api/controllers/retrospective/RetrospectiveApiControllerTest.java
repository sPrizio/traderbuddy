package com.traderbuddyv2.api.controllers.retrospective;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.constants.ApiConstants;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.services.plan.TradingPlanService;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import com.traderbuddyv2.core.services.retrospective.RetrospectiveService;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import com.traderbuddyv2.core.services.trade.record.TradeRecordService;
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
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link RetrospectiveApiController}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class RetrospectiveApiControllerTest extends AbstractGenericTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetrospectiveService retrospectiveService;

    @MockBean
    private TradeRecordService tradeRecordService;

    @MockBean
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @MockBean
    private TradingPlanService tradingPlanService;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setUp() {
        Mockito.when(this.retrospectiveService.findAllRetrospectivesWithinDate(any(), any(), any())).thenReturn(generateRetrospectives());
        Mockito.when(this.retrospectiveService.findRetrospectiveForStartDateAndEndDateAndInterval(LocalDate.of(2022, 9, 5), LocalDate.of(2022, 9, 6), AggregateInterval.MONTHLY)).thenReturn(Optional.empty());
        Mockito.when(this.retrospectiveService.findRetrospectiveForStartDateAndEndDateAndInterval(LocalDate.of(2022, 9, 10), LocalDate.of(2022, 9, 15), AggregateInterval.MONTHLY)).thenReturn(Optional.of(generateRetrospectives().get(1)));
        Mockito.when(this.retrospectiveService.findRetrospectiveForUid("test")).thenReturn(Optional.of(generateRetrospectives().get(1)));
        Mockito.when(this.retrospectiveService.findRetrospectiveForUid("BAD")).thenReturn(Optional.empty());
        Mockito.when(this.retrospectiveService.findActiveRetrospectiveMonths(anyInt())).thenReturn(List.of(LocalDate.of(2022, 9, 1)));
        Mockito.when(this.retrospectiveService.findActiveRetrospectiveYears()).thenReturn(List.of(LocalDate.of(2022, 1, 1)));
        Mockito.when(this.retrospectiveService.findMostRecentRetrospectiveForInterval(AggregateInterval.WEEKLY)).thenReturn(Optional.empty());
        Mockito.when(this.retrospectiveService.findMostRecentRetrospectiveForInterval(AggregateInterval.MONTHLY)).thenReturn(Optional.of(generateRetrospectives().get(0)));
        Mockito.when(this.retrospectiveService.createRetrospective(any())).thenReturn(generateRetrospectives().get(0));
        Mockito.when(this.retrospectiveService.updateRetrospective(any(), any())).thenReturn(generateRetrospectives().get(1));
        Mockito.when(this.retrospectiveService.deleteRetrospective(any())).thenReturn(true);
        Mockito.when(this.tradingPlanService.findCurrentlyActiveTradingPlan()).thenReturn(Optional.of(generateTestTradingPlan()));
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.traderBuddyUserDetailsService.getCurrentUser()).thenReturn(generateTestUser());
        Mockito.when(this.tradeRecordService.findTradeRecordForStartDateAndEndDateAndInterval(any(), any(), any())).thenReturn(Optional.empty());
        Mockito.when(this.retrospectiveService.saveAudio(any(), any(), any(), any(), any())).thenReturn("");
    }


    //  ----------------- getRetrospectivesForTimespan -----------------

    @Test
    public void test_getRetrospectivesForTimespan_badRequest_start() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("asdasdasdasd"));
        map.put("end", List.of("2022-09-18"));
        map.put("interval", List.of("MONTHLY"));

        this.mockMvc.perform(get("/api/v1/retrospective/timespan").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_getRetrospectivesForTimespan_badRequest_end() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-09-12"));
        map.put("end", List.of("adfafdsfdsfsd"));
        map.put("interval", List.of("MONTHLY"));

        this.mockMvc.perform(get("/api/v1/retrospective/timespan").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_getRetrospectivesForTimespan_badRequest_interval() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-09-12"));
        map.put("end", List.of("2022-09-18"));
        map.put("interval", List.of("BAD"));

        this.mockMvc.perform(get("/api/v1/retrospective/timespan").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Looks like your request could not be processed. Check your inputs and try again!")));
    }

    @Test
    public void test_getRetrospectivesForTimespan_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-09-12"));
        map.put("end", List.of("2022-09-18"));
        map.put("interval", List.of("MONTHLY"));

        this.mockMvc.perform(get("/api/v1/retrospective/timespan").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].startDate", is("2022-09-05")));
    }


    //  ----------------- getRetrospectiveForStartDateAndEndDateAndInterval -----------------

    @Test
    public void test_getRetrospectiveForStartDateAndEndDateAndInterval_badRequest_start() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("asdasdasdasd"));
        map.put("end", List.of("2022-09-18"));
        map.put("interval", List.of("MONTHLY"));

        this.mockMvc.perform(get("/api/v1/retrospective/unique").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_getRetrospectiveForStartDateAndEndDateAndInterval_badRequest_end() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-09-12"));
        map.put("end", List.of("adfafdsfdsfsd"));
        map.put("interval", List.of("MONTHLY"));

        this.mockMvc.perform(get("/api/v1/retrospective/unique").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_getRetrospectiveForStartDateAndEndDateAndInterval_badRequest_interval() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-09-05"));
        map.put("end", List.of("2022-09-18"));
        map.put("interval", List.of("BAD"));

        this.mockMvc.perform(get("/api/v1/retrospective/unique").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Looks like your request could not be processed. Check your inputs and try again!")));
    }

    @Test
    public void test_getRetrospectiveForStartDateAndEndDateAndInterval_noResult() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-09-05"));
        map.put("end", List.of("2022-09-06"));
        map.put("interval", List.of("MONTHLY"));

        this.mockMvc.perform(get("/api/v1/retrospective/unique").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("No retrospective was found for start date 2022-09-05, end date 2022-09-06 and interval MONTHLY")));
    }

    @Test
    public void test_getRetrospectiveForStartDateAndEndDateAndInterval_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-09-10"));
        map.put("end", List.of("2022-09-15"));
        map.put("interval", List.of("MONTHLY"));

        this.mockMvc.perform(get("/api/v1/retrospective/unique").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.startDate", is("2022-09-12")));
    }


    //  ----------------- getRetrospectiveForUid -----------------

    @Test
    public void test_getRetrospectiveForUid_noResult() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("uid", List.of("BAD"));

        this.mockMvc.perform(get("/api/v1/retrospective/uid").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("No retrospective was found for uid BAD")));
    }

    @Test
    public void test_getRetrospectiveForUid_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("uid", List.of("test"));

        this.mockMvc.perform(get("/api/v1/retrospective/uid").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.startDate", is("2022-09-12")));
    }


    //  ----------------- getActiveRetrospectiveMonths -----------------

    @Test
    public void test_getActiveRetrospectiveMonths_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("year", List.of("2022"));
        map.put("includeStarterYear", List.of("true"));

        this.mockMvc.perform(get("/api/v1/retrospective/active-months").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]", is("2022-09-01")));
    }


    //  ----------------- getActiveRetrospectiveYears -----------------

    @Test
    public void test_getActiveRetrospectiveYears_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/retrospective/active-years"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0]", is("2022-01-01")));
    }


    //  ----------------- getMostRecentRetrospectiveForInterval -----------------

    @Test
    public void test_getMostRecentRetrospectiveForInterval_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("interval", List.of("BAD"));

        this.mockMvc.perform(get("/api/v1/retrospective/most-recent").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("BAD was not a valid interval")));
    }

    @Test
    public void test_getMostRecentRetrospectiveForInterval_success_no_results() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("interval", List.of("WEEKLY"));

        this.mockMvc.perform(get("/api/v1/retrospective/most-recent").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("No recent retrospectives were found for interval : WEEKLY")));
    }

    @Test
    public void test_getMostRecentRetrospectiveForInterval_success() throws Exception {
        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("interval", List.of("MONTHLY"));

        this.mockMvc.perform(get("/api/v1/retrospective/most-recent").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.startDate", is("2022-09-05")));
    }


    //  ----------------- postCreateRetrospective -----------------

    @Test
    public void test_postCreateRetrospective_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post("/api/v1/retrospective/create").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_postCreateRetrospective_success() throws Exception {

        Map<String, Object> data =
                Map.of(
                        "retrospective",
                        Map.of(
                                "startDate", "2022-09-05",
                                "endDate", "2022-09-11",
                                "interval", "MONTHLY",
                                "points", List.of(
                                        Map.of(
                                                "lineNumber", 1,
                                                "entryText", "Test 1",
                                                "keyPoint", "true"
                                        ),
                                        Map.of(
                                                "lineNumber", 2,
                                                "entryText", "Test 2",
                                                "keyPoint", "false"
                                        )
                                )
                        )
                );

        this.mockMvc.perform(post("/api/v1/retrospective/create").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.startDate", is("2022-09-05")));
    }


    //  ----------------- postUploadAudio -----------------

    @Test
    public void test_postUploadAudio_success() throws Exception {

        final MockMultipartFile file = new MockMultipartFile("file", "hello.mp3", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.put("start", List.of("2023-02-01"));
        params.put("end", List.of("2023-03-01"));
        params.put("interval", List.of("MONTHLY"));
        params.put("name", List.of("Test"));

        this.mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/retrospective/upload-audio").file(file).params(params))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }


    //  ----------------- putUpdateRetrospective -----------------

    @Test
    public void test_putUpdateRetrospective_badJsonIntegrity() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("uid", List.of("test"));

        this.mockMvc.perform(put("/api/v1/retrospective/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_putUpdateRetrospective_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("uid", List.of("test"));

        Map<String, Object> data =
                Map.of(
                        "retrospective",
                        Map.of(
                                "startDate", "2022-09-05",
                                "endDate", "2022-09-11",
                                "interval", "MONTHLY",
                                "points", List.of(
                                        Map.of(
                                                "lineNumber", 1,
                                                "entryText", "Test 1",
                                                "keyPoint", "true"
                                        ),
                                        Map.of(
                                                "lineNumber", 2,
                                                "entryText", "Test 2",
                                                "keyPoint", "false"
                                        )
                                )
                        )
                );

        this.mockMvc.perform(put("/api/v1/retrospective/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.startDate", is("2022-09-12")));
    }


    //  ----------------- deleteRetrospective -----------------

    @Test
    public void test_deleteRetrospective_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("uid", List.of("test"));

        this.mockMvc.perform(delete("/api/v1/retrospective/delete").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(true)));
    }
}
