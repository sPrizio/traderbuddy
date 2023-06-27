package com.traderbuddyv2.api.controllers.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.constants.ApiConstants;
import com.traderbuddyv2.api.converters.account.AccountDTOConverter;
import com.traderbuddyv2.api.facades.AccountFacade;
import com.traderbuddyv2.core.enums.account.StopLimitType;
import com.traderbuddyv2.core.models.records.account.LossInfo;
import com.traderbuddyv2.core.services.account.AccountService;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link AccountApiController}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class AccountApiControllerTest extends AbstractGenericTest {

    @MockBean
    private AccountFacade accountFacade;

    @MockBean
    private AccountDTOConverter accountDTOConverter;

    @MockBean
    private AccountService accountService;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        Mockito.when(this.accountFacade.getAccountOverview()).thenReturn(generateAccountOverview());
        Mockito.when(this.accountService.getEquityCurve(any(), anyInt())).thenReturn(List.of());
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.accountService.findAccountBalanceHistory(any(), any())).thenReturn(List.of(generateTestAccountBalanceModification()));
        Mockito.when(this.accountService.deleteAccountBalanceModification(anyString())).thenReturn(true);
        Mockito.when(this.accountService.createAccountBalanceModification(any())).thenReturn(generateTestAccountBalanceModification());
        Mockito.when(this.accountService.getPromoPayments()).thenReturn(List.of(generateTestBuyTrade()));
        Mockito.when(this.accountService.getLossInfo(any(), any())).thenReturn(new LossInfo(StopLimitType.POINTS, 1.0, 1.0, 1.0, 1));
        Mockito.when(this.accountService.updateDefaultAccount(anyLong())).thenReturn(true);
        Mockito.when(this.accountService.createNewAccount(any())).thenReturn(generateTestAccount());
        Mockito.when(this.accountDTOConverter.convert(any())).thenReturn(generateTestAccountDTO());
    }


    //  ----------------- getAccountOverview -----------------

    @Test
    public void test_getAccountOverview_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/account/overview"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.balance", is(1025.0)))
                .andExpect(jsonPath("$.data.dateTime", is("2022-08-24T00:00:00")));
    }


    //  ----------------- getAccountEquityCurve -----------------

    @Test
    public void test_getAccountEquityCurve_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("interval", List.of("DAILY"));
        map.put("count", List.of("1"));

        this.mockMvc.perform(get("/api/v1/account/equity-curve").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }


    //  ----------------- getAccountBalanceHistory -----------------

    @Test
    public void test_getAccountBalanceHistory_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24"));
        map.put("end", List.of("2022-08-25"));

        this.mockMvc.perform(get("/api/v1/account/balance-history").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }


    //  ----------------- getPromotionalPayments -----------------

    @Test
    public void test_getPromotionalPayments_success() throws Exception {

        this.mockMvc.perform(get("/api/v1/account/promo-payments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total", is(14.85)));
    }


    //  ----------------- getLossInfo -----------------

    @Test
    public void test_getLossInfo_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2022-08-24"));
        map.put("end", List.of("2022-08-25"));

        this.mockMvc.perform(get("/api/v1/account/loss-info").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.occurrences", is(1)));
    }


    //  ----------------- getCurrencies -----------------

    @Test
    public void test_getCurrencies_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/account/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is("USD")));
    }


    //  ----------------- getAccountTypes -----------------

    @Test
    public void test_getAccountTypes_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/account/account-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is("SHARES")));
    }


    //  ----------------- getBrokers -----------------

    @Test
    public void test_getBrokers_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/account/brokers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is("CMC_MARKETS")));
    }


    //  ----------------- getDailyStopTypes -----------------

    @Test
    public void test_getDailyStopTypes_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/account/stop-types"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is("PROFIT")));
    }


    //  ----------------- getTradePlatforms -----------------

    @Test
    public void test_getTradePlatforms_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/account/trade-platforms"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].code", is("CMC_MARKETS")));
    }


    //  ----------------- putSwitchAccount -----------------

    @Test
    public void test_putSwitchAccount_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("accountNumber", List.of("1234"));

        this.mockMvc.perform(put("/api/v1/account/switch-account").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(true)));
    }


    //  ----------------- postCreateAccountBalanceModification -----------------

    @Test
    public void test_postCreateAccountBalanceModification_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post("/api/v1/account/create-modification").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_postCreateAccountBalanceModification_success() throws Exception {

        Map<String, Object> data =
                Map.of(
                        "modification",
                        Map.of(
                                "dateTime", "2022-09-12",
                                "amount", "123.45",
                                "type", "0",
                                "description", "Test"
                        )
                );

        this.mockMvc.perform(post("/api/v1/account/create-modification").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.dateTime", is("2022-09-12T01:01:01")));
    }


    //  ----------------- postCreateNewAccount -----------------

    @Test
    public void test_postCreateNewAccount_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post("/api/v1/account/create-account").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_postCreateNewAccount_success() throws Exception {

        Map<String, Object> data =
                Map.of(
                        "account",
                        Map.of(
                                "name", "Test",
                                "number", "123",
                                "balance", "150",
                                "currency", "CAD",
                                "type", "CFD",
                                "broker", "CMC_MARKETS",
                                "dailyStop", "55",
                                "dailyStopType", "POINTS",
                                "tradePlatform", "METATRADER4"
                        )
                );

        this.mockMvc.perform(post("/api/v1/account/create-account").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.balance", is(1000.0)));
    }


    //  ----------------- deleteAccountBalanceModification -----------------

    @Test
    public void test_deleteAccountBalanceModification_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("uid", List.of("test"));

        this.mockMvc.perform(delete("/api/v1/account/delete-modification").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(true)));
    }
}
