package com.traderbuddyv2.api.controllers.news;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.services.news.MarketNewsService;
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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link MarketNewsApiController}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class MarketNewsApiControllerTest extends AbstractGenericTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MarketNewsService marketNewsService;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.marketNewsService.findNewsWithinInterval(any(), any())).thenReturn(List.of(generateMarketNews()));
    }


    //  ----------------- getNewsForInterval -----------------

    @Test
    public void test_getNewsForInterval_badRequest() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("BAD"));
        map.put("end", List.of("2023-01-21"));

        this.mockMvc.perform(get("/api/v1/news/for-interval").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString("Looks like your request could not be processed. Check your inputs and try again!")));
    }

    @Test
    public void test_getNewsForInterval_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("start", List.of("2023-01-16"));
        map.put("end", List.of("2023-01-21"));

        this.mockMvc.perform(get("/api/v1/news/for-interval").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].date", is("2023-01-19")));
    }
}
