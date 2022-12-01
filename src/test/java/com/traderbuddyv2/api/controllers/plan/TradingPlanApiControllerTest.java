package com.traderbuddyv2.api.controllers.plan;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.constants.ApiConstants;
import com.traderbuddyv2.core.enums.plans.TradingPlanStatus;
import com.traderbuddyv2.core.models.entities.plan.TradingPlan;
import com.traderbuddyv2.core.services.plan.TradingPlanService;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
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
import java.util.Optional;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link TradingPlanApiController}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class TradingPlanApiControllerTest extends AbstractGenericTest {

    private final TradingPlan TEST_TRADING_PLAN_ACTIVE = generateTestTradingPlan();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradingPlanService tradingPlanService;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Before
    public void setUp() {
        Mockito.when(this.tradingPlanService.findCurrentlyActiveTradingPlan()).thenReturn(Optional.of(TEST_TRADING_PLAN_ACTIVE));
        Mockito.when(this.tradingPlanService.findTradingPlansForStatus(TradingPlanStatus.IN_PROGRESS)).thenReturn(List.of(TEST_TRADING_PLAN_ACTIVE));
        Mockito.when(this.tradingPlanService.createTradingPlan(any())).thenReturn(TEST_TRADING_PLAN_ACTIVE);
        Mockito.when(this.tradingPlanService.updateTradingPlan(any(), any(), any(), any())).thenReturn(TEST_TRADING_PLAN_ACTIVE);
        Mockito.when(this.tradingPlanService.forecast(any(), any(), any(), any())).thenReturn(generateForecast());
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- getCurrentlyActiveTradingPlan -----------------

    @Test
    public void test_getCurrentlyActiveTradingPlan_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/trading-plan/current"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.profitTarget", is(1.25)));
    }


    //  ----------------- getCurrentlyActiveTradingPlan -----------------

    @Test
    public void test_getTradesForTradeType_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("status", List.of("BAD"));

        this.mockMvc.perform(get("/api/v1/trading-plan/for-status").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("BAD is not a valid status")));
    }

    @Test
    public void test_getTradingPlansForStatus_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("status", List.of("IN_PROGRESS"));

        this.mockMvc.perform(get("/api/v1/trading-plan/for-status").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].profitTarget", is(1.25)));
    }


    //  ----------------- getForecast -----------------

    @Test
    public void test_getForecast_badRequest_interval() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("interval", List.of("BAD"));
        map.put("begin", List.of("2022-01-01"));
        map.put("limit", List.of("2022-01-01"));

        this.mockMvc.perform(get("/api/v1/trading-plan/forecast").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("No active trading plan was found for interval BAD, start 2022-01-01, end 2022-01-01")));
    }

    @Test
    public void test_getForecast_badRequest_begin() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("interval", List.of("MONTHLY"));
        map.put("begin", List.of("adfsffaf"));
        map.put("limit", List.of("2022-01-01"));


        this.mockMvc.perform(get("/api/v1/trading-plan/forecast").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_getForecast_badRequest_limit() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("interval", List.of("MONTHLY"));
        map.put("begin", List.of("2022-01-01"));
        map.put("limit", List.of("adfsffaf"));

        this.mockMvc.perform(get("/api/v1/trading-plan/forecast").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_getForecast_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("interval", List.of("MONTHLY"));
        map.put("begin", List.of("2022-01-01"));
        map.put("limit", List.of("2022-01-01"));

        this.mockMvc.perform(get("/api/v1/trading-plan/forecast").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.entries[0].earnings", is(1.0)));
    }
}
