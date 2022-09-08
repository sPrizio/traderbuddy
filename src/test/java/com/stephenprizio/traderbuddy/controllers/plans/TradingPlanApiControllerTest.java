package com.stephenprizio.traderbuddy.controllers.plans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.enums.plans.TradingPlanStatus;
import com.stephenprizio.traderbuddy.models.entities.plans.TradingPlan;
import com.stephenprizio.traderbuddy.services.plans.TradingPlanService;
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
 * Testing class for {@link TradingPlanApiController}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
public class TradingPlanApiControllerTest extends AbstractGenericTest {

    private final TradingPlan TEST_TRADING_PLAN_ACTIVE = generateTestTradingPlan();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradingPlanService tradingPlanService;

    @Before
    public void setUp() {
        Mockito.when(this.tradingPlanService.findCurrentlyActiveTradingPlan()).thenReturn(Optional.of(TEST_TRADING_PLAN_ACTIVE));
        Mockito.when(this.tradingPlanService.findTradingPlansForStatus(TradingPlanStatus.IN_PROGRESS)).thenReturn(List.of(TEST_TRADING_PLAN_ACTIVE));
        Mockito.when(this.tradingPlanService.createTradingPlan(any())).thenReturn(TEST_TRADING_PLAN_ACTIVE);
        Mockito.when(this.tradingPlanService.updateTradingPlan(any(), any(), any(), any())).thenReturn(TEST_TRADING_PLAN_ACTIVE);
    }


    //  ----------------- getCurrentlyActiveTradingPlan -----------------

    @Test
    public void test_getCurrentlyActiveTradingPlan_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/trading-plans/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.profitTarget", is(528491.0)));
    }


    //  ----------------- getCurrentlyActiveTradingPlan -----------------

    @Test
    public void test_getTradesForTradeType_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("status", List.of("BAD"));

        this.mockMvc.perform(get("/api/v1/trading-plans/for-status").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("BAD is not a valid status")));
    }

    @Test
    public void test_getTradingPlansForStatus_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("status", List.of("IN_PROGRESS"));

        this.mockMvc.perform(get("/api/v1/trading-plans/for-status").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].profitTarget", is(528491.0)));
    }


    //  ----------------- postCreateTradingPlan -----------------

    @Test
    public void test_postCreateTradingPlan_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post("/api/v1/trading-plans/create").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("json did not contain of the required keys : [status, active, name, startDate, endDate, profitTarget, compoundFrequency, startingBalance]")));
    }

    @Test
    public void test_postCreateTradingPlan_success() throws Exception {

        Map<String, Object> data =
                Map.of(
                        "status", TradingPlanStatus.COMPLETED,
                        "active", true,
                        "name", "updated name",
                        "startDate", "2024-02-02",
                        "endDate", "2024-02-02",
                        "profitTarget", 123.0,
                        "compoundFrequency", CompoundFrequency.DAILY,
                        "startingBalance", 1000.0
                );

        this.mockMvc.perform(post("/api/v1/trading-plans/create").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.profitTarget", is(528491.0)))
                .andExpect(jsonPath("$.data.name", is("Test Trading Plan Active")));
    }


    //  ----------------- putUpdateTradingPlan -----------------

    @Test
    public void test_putUpdateTradingPlan_badJsonIntegrity() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("name", List.of("name"));
        map.put("startDate", List.of("2022-08-23"));
        map.put("endDate", List.of("2022-08-25"));

        this.mockMvc.perform(put("/api/v1/trading-plans/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("json did not contain of the required keys : [status, active, name, startDate, endDate, profitTarget, compoundFrequency, startingBalance]")));
    }

    @Test
    public void test_putUpdateTradingPlan_missingParamStart() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("name", List.of("name"));
        map.put("startDate", List.of("dasdfasdfaf"));
        map.put("endDate", List.of("2022-08-25"));

        Map<String, Object> data =
                Map.of(
                        "status", TradingPlanStatus.COMPLETED,
                        "active", true,
                        "name", "updated name",
                        "startDate", "2024-02-02",
                        "endDate", "2024-02-02",
                        "profitTarget", 123.0,
                        "compoundFrequency", CompoundFrequency.DAILY,
                        "startingBalance", 1000.0
                );

        this.mockMvc.perform(put("/api/v1/trading-plans/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The start date dasdfasdfaf was not of the expected format yyyy-MM-dd")));
    }

    @Test
    public void test_putUpdateTradingPlan_missingParamEnd() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("name", List.of("name"));
        map.put("startDate", List.of("2022-08-25"));
        map.put("endDate", List.of("asdadasdasd"));

        Map<String, Object> data =
                Map.of(
                        "status", TradingPlanStatus.COMPLETED,
                        "active", true,
                        "name", "updated name",
                        "startDate", "2024-02-02",
                        "endDate", "2024-02-02",
                        "profitTarget", 123.0,
                        "compoundFrequency", CompoundFrequency.DAILY,
                        "startingBalance", 1000.0
                );

        this.mockMvc.perform(put("/api/v1/trading-plans/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("The end date asdadasdasd was not of the expected format yyyy-MM-dd")));
    }

    @Test
    public void test_putUpdateTradingPlan_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("name", List.of("name"));
        map.put("startDate", List.of("2022-08-25"));
        map.put("endDate", List.of("2022-08-27"));

        Map<String, Object> data =
                Map.of(
                        "status", TradingPlanStatus.COMPLETED,
                        "active", true,
                        "name", "updated name",
                        "startDate", "2024-02-02",
                        "endDate", "2024-02-02",
                        "profitTarget", 123.0,
                        "compoundFrequency", CompoundFrequency.DAILY,
                        "startingBalance", 1000.0
                );

        this.mockMvc.perform(put("/api/v1/trading-plans/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.profitTarget", is(528491.0)))
                .andExpect(jsonPath("$.data.name", is("Test Trading Plan Active")));
    }
}
