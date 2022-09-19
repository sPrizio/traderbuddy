package com.stephenprizio.traderbuddy.controllers.retrospectives;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.services.retrospectives.RetrospectiveService;
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

import java.time.LocalDate;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
                .andExpect(jsonPath("$.data[0].startDate", is(LocalDate.of(2022, 9, 5))));
    }
}
