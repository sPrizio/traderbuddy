package com.traderbuddyv2.api.controllers.account;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.services.account.AccountService;
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

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    private AccountService accountService;

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp() {
        Mockito.when(this.accountService.getAccountOverview()).thenReturn(generateAccountOverview());
        Mockito.when(this.accountService.getEquityCurve(any(), any(), any())).thenReturn(List.of());
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
}
