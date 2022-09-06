package com.stephenprizio.traderbuddy.controllers.goals;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.enums.goals.GoalStatus;
import com.stephenprizio.traderbuddy.models.entities.goals.Goal;
import com.stephenprizio.traderbuddy.services.goals.GoalService;
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
 * Testing class for {@link GoalApiController}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class GoalApiControllerTest extends AbstractGenericTest {

    private final Goal TEST_GOAL_ACTIVE = generateTestGoal();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GoalService goalService;

    @Before
    public void setUp() {
        Mockito.when(this.goalService.findCurrentlyActiveGoal()).thenReturn(Optional.of(TEST_GOAL_ACTIVE));
        Mockito.when(this.goalService.findGoalsForStatus(GoalStatus.IN_PROGRESS)).thenReturn(List.of(TEST_GOAL_ACTIVE));
        Mockito.when(this.goalService.createGoal(any())).thenReturn(TEST_GOAL_ACTIVE);
        Mockito.when(this.goalService.updateGoal(any(), any(), any(), any())).thenReturn(TEST_GOAL_ACTIVE);
    }


    //  ----------------- getCurrentlyActiveGoal -----------------

    @Test
    public void test_getCurrentlyActiveGoal_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/goals/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.profitTarget", is(528491.0)));
    }


    //  ----------------- getCurrentlyActiveGoal -----------------

    @Test
    public void test_getTradesForTradeType_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("status", List.of("BAD"));

        this.mockMvc.perform(get("/api/v1/goals/for-status").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("BAD is not a valid status")));
    }

    @Test
    public void test_getGoalsForStatus_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("status", List.of("IN_PROGRESS"));

        this.mockMvc.perform(get("/api/v1/goals/for-status").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].profitTarget", is(528491.0)));
    }


    //  ----------------- postCreateGoal -----------------

    @Test
    public void test_postCreateGoal_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post("/api/v1/goals/create").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("json did not contain of the required keys : [status, active, name, startDate, endDate, profitTarget, compoundFrequency, startingBalance]")));
    }

    @Test
    public void test_postCreateGoal_success() throws Exception {

        Map<String, Object> data =
                Map.of(
                        "status", GoalStatus.COMPLETED,
                        "active", true,
                        "name", "updated name",
                        "startDate", "2024-02-02",
                        "endDate", "2024-02-02",
                        "profitTarget", 123.0,
                        "compoundFrequency", CompoundFrequency.DAILY,
                        "startingBalance", 1000.0
                );

        this.mockMvc.perform(post("/api/v1/goals/create").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.profitTarget", is(528491.0)))
                .andExpect(jsonPath("$.data.name", is("Test Goal Active")));
    }


    //  ----------------- putUpdateGoal -----------------

    @Test
    public void test_putUpdateGoal_badJsonIntegrity() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("name", List.of("name"));
        map.put("startDate", List.of("2022-08-23"));
        map.put("endDate", List.of("2022-08-25"));

        this.mockMvc.perform(put("/api/v1/goals/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("json did not contain of the required keys : [status, active, name, startDate, endDate, profitTarget, compoundFrequency, startingBalance]")));
    }

    @Test
    public void test_putUpdateGoal_missingParamStart() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("name", List.of("name"));
        map.put("startDate", List.of("dasdfasdfaf"));
        map.put("endDate", List.of("2022-08-25"));

        Map<String, Object> data =
                Map.of(
                        "status", GoalStatus.COMPLETED,
                        "active", true,
                        "name", "updated name",
                        "startDate", "2024-02-02",
                        "endDate", "2024-02-02",
                        "profitTarget", 123.0,
                        "compoundFrequency", CompoundFrequency.DAILY,
                        "startingBalance", 1000.0
                );

        this.mockMvc.perform(put("/api/v1/goals/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The start date dasdfasdfaf was not of the expected format yyyy-MM-dd")));
    }

    @Test
    public void test_putUpdateGoal_missingParamEnd() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("name", List.of("name"));
        map.put("startDate", List.of("2022-08-25"));
        map.put("endDate", List.of("asdadasdasd"));

        Map<String, Object> data =
                Map.of(
                        "status", GoalStatus.COMPLETED,
                        "active", true,
                        "name", "updated name",
                        "startDate", "2024-02-02",
                        "endDate", "2024-02-02",
                        "profitTarget", 123.0,
                        "compoundFrequency", CompoundFrequency.DAILY,
                        "startingBalance", 1000.0
                );

        this.mockMvc.perform(put("/api/v1/goals/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The end date asdadasdasd was not of the expected format yyyy-MM-dd")));
    }

    @Test
    public void test_putUpdateGoal_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("name", List.of("name"));
        map.put("startDate", List.of("2022-08-25"));
        map.put("endDate", List.of("2022-08-27"));

        Map<String, Object> data =
                Map.of(
                        "status", GoalStatus.COMPLETED,
                        "active", true,
                        "name", "updated name",
                        "startDate", "2024-02-02",
                        "endDate", "2024-02-02",
                        "profitTarget", 123.0,
                        "compoundFrequency", CompoundFrequency.DAILY,
                        "startingBalance", 1000.0
                );

        this.mockMvc.perform(put("/api/v1/goals/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.profitTarget", is(528491.0)))
                .andExpect(jsonPath("$.data.name", is("Test Goal Active")));
    }
}
