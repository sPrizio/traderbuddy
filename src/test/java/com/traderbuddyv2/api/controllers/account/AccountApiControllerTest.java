package com.traderbuddyv2.api.controllers.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.constants.ApiConstants;
import com.traderbuddyv2.api.facades.AccountFacade;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    private AccountService accountService;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        Mockito.when(this.accountFacade.getAccountOverview()).thenReturn(generateAccountOverview());
        Mockito.when(this.accountService.getEquityCurve(any(), any(), any())).thenReturn(List.of());
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.accountService.findAccountBalanceHistory(any(), any())).thenReturn(List.of(generateTestAccountBalanceModification()));
        Mockito.when(this.accountService.deleteAccountBalanceModification(anyString())).thenReturn(true);
        Mockito.when(this.accountService.createAccountBalanceModification(any())).thenReturn(generateTestAccountBalanceModification());
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
        map.put("start", List.of("2022-08-24"));
        map.put("end", List.of("2022-08-25"));
        map.put("interval", List.of("DAILY"));

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
                                "dateTime", "2022-09-12T11:08:53",
                                "amount", "123.45",
                                "type", "0",
                                "description", "Test"
                        )
                );

        this.mockMvc.perform(post("/api/v1/account/create-modification").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.dateTime", is("2022-09-12T01:01:01")));
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
