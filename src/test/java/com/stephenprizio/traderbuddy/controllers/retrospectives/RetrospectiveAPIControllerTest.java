package com.stephenprizio.traderbuddy.controllers.retrospectives;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import com.stephenprizio.traderbuddy.services.retrospectives.RetrospectiveService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link RetrospectiveAPIController}
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class RetrospectiveAPIControllerTest extends AbstractGenericTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RetrospectiveService retrospectiveService;

    @Before
    public void setUp() {
        Mockito.when(this.retrospectiveService.findAllRetrospectivesWithinDate(any(), any())).thenReturn(generateRetrospectives());
        Mockito.when(this.retrospectiveService.findRetrospectiveForStartDateAndEndDateAndInterval(LocalDate.of(2022, 9, 5), LocalDate.of(2022, 9, 6), AggregateInterval.MONTHLY)).thenReturn(Optional.empty());
        Mockito.when(this.retrospectiveService.findRetrospectiveForStartDateAndEndDateAndInterval(LocalDate.of(2022, 9, 10), LocalDate.of(2022, 9, 15), AggregateInterval.MONTHLY)).thenReturn(Optional.of(generateRetrospectives().get(1)));
        Mockito.when(this.retrospectiveService.createRetrospective(any())).thenReturn(generateRetrospectives().get(0));
        Mockito.when(this.retrospectiveService.updateRetrospective(any(), any(), any(), any())).thenReturn(generateRetrospectives().get(1));
    }


    //  ----------------- getRetrospectivesForTimespan -----------------

    @Test
    public void test_getRetrospectivesForTimespan_badRequest_start() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("asdasdasdasd"));
        map.put("end", List.of("2022-09-18"));

        this.mockMvc.perform(get("/api/v1/retrospectives/timespan").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The start date asdasdasdasd was not of the expected format yyyy-MM-dd")));
    }

    @Test
    public void test_getRetrospectivesForTimespan_badRequest_end() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-09-12"));
        map.put("end", List.of("adfafdsfdsfsd"));

        this.mockMvc.perform(get("/api/v1/retrospectives/timespan").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The end date adfafdsfdsfsd was not of the expected format yyyy-MM-dd")));
    }

    @Test
    public void test_getRetrospectivesForTimespan_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-09-12"));
        map.put("end", List.of("2022-09-18"));

        this.mockMvc.perform(get("/api/v1/retrospectives/timespan").params(map))
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

        this.mockMvc.perform(get("/api/v1/retrospectives/unique").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The start date asdasdasdasd was not of the expected format yyyy-MM-dd")));
    }

    @Test
    public void test_getRetrospectiveForStartDateAndEndDateAndInterval_badRequest_end() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-09-12"));
        map.put("end", List.of("adfafdsfdsfsd"));
        map.put("interval", List.of("MONTHLY"));

        this.mockMvc.perform(get("/api/v1/retrospectives/unique").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The end date adfafdsfdsfsd was not of the expected format yyyy-MM-dd")));
    }

    @Test
    public void test_getRetrospectiveForStartDateAndEndDateAndInterval_badRequest_interval() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-09-05"));
        map.put("end", List.of("2022-09-18"));
        map.put("interval", List.of("BAD"));

        this.mockMvc.perform(get("/api/v1/retrospectives/unique").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("BAD was not a valid interval")));
    }

    @Test
    public void test_getRetrospectiveForStartDateAndEndDateAndInterval_noResult() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-09-05"));
        map.put("end", List.of("2022-09-06"));
        map.put("interval", List.of("MONTHLY"));

        this.mockMvc.perform(get("/api/v1/retrospectives/unique").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("No retrospective was found for start date 2022-09-05, end date 2022-09-06 and interval MONTHLY")));
    }

    @Test
    public void test_getRetrospectiveForStartDateAndEndDateAndInterval_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-09-10"));
        map.put("end", List.of("2022-09-15"));
        map.put("interval", List.of("MONTHLY"));

        this.mockMvc.perform(get("/api/v1/retrospectives/unique").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.startDate", is("2022-09-12")));
    }


    //  ----------------- postCreateRetrospective -----------------

    @Test
    public void test_postCreateRetrospective_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post("/api/v1/retrospectives/create").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("json did not contain of the required keys : [retrospective]")));
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

        this.mockMvc.perform(post("/api/v1/retrospectives/create").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.startDate", is("2022-09-05")));
    }


    //  ----------------- putUpdateRetrospective -----------------

    @Test
    public void test_putUpdateRetrospective_badRequest_start() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("asdasdasdasd"));
        map.put("end", List.of("2022-09-18"));
        map.put("interval", List.of("MONTHLY"));

        this.mockMvc.perform(put("/api/v1/retrospectives/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("retrospective", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The start date asdasdasdasd was not of the expected format yyyy-MM-dd")));
    }

    @Test
    public void test_putUpdateRetrospective_badRequest_end() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-09-12"));
        map.put("end", List.of("adfafdsfdsfsd"));
        map.put("interval", List.of("MONTHLY"));

        this.mockMvc.perform(put("/api/v1/retrospectives/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("retrospective", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The end date adfafdsfdsfsd was not of the expected format yyyy-MM-dd")));
    }

    @Test
    public void test_putUpdateRetrospective_badRequest_interval() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-09-05"));
        map.put("end", List.of("2022-09-18"));
        map.put("interval", List.of("BAD"));

        this.mockMvc.perform(put("/api/v1/retrospectives/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("retrospective", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("BAD was not a valid interval")));
    }

    @Test
    public void test_putUpdateRetrospective_badJsonIntegrity() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-09-10"));
        map.put("end", List.of("2022-09-15"));
        map.put("interval", List.of("MONTHLY"));

        this.mockMvc.perform(put("/api/v1/retrospectives/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("json did not contain of the required keys : [retrospective]")));
    }

    @Test
    public void test_putUpdateRetrospective_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-09-10"));
        map.put("end", List.of("2022-09-15"));
        map.put("interval", List.of("MONTHLY"));

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

        this.mockMvc.perform(put("/api/v1/retrospectives/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.startDate", is("2022-09-12")));
    }
}
