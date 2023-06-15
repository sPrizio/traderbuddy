package com.traderbuddyv2.api.controllers.news;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.constants.ApiConstants;
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
        Mockito.when(this.marketNewsService.findNewsWithinInterval(any(), any(), any())).thenReturn(List.of(generateMarketNews()));
        Mockito.when(this.marketNewsService.createMarketNews(anyMap())).thenReturn(generateMarketNews());
        Mockito.when(this.marketNewsService.updateMarketNews(anyString(), anyMap())).thenReturn(generateMarketNews());
        Mockito.when(this.marketNewsService.deleteMarketNews(anyString())).thenReturn(true);
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
        map.put("locales", List.of());

        this.mockMvc.perform(get("/api/v1/news/for-interval").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].date", is("2023-01-19")));
    }


    //  ----------------- getNewsLocales -----------------

    @Test
    public void test_getNewsLocales_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/news/locales"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].middle", is("ARG")));
    }

    
    //  ----------------- postCreateNews -----------------

    @Test
    public void test_postCreateNews_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post("/api/v1/news/create").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_postCreateNews_success() throws Exception {

        Map<String, Object> data =
                Map.of(
                        "marketNews",
                        Map.of(
                                "date", "2022-09-05",
                                "slots", List.of(
                                        Map.of(
                                                "time", "14:00",
                                                "entries", List.of(
                                                        Map.of(
                                                                "content", "Test News Entry 1",
                                                                "severity", 3
                                                        ),
                                                        Map.of(
                                                                "content", "Test News Entry 2",
                                                                "severity", 2
                                                        )
                                                )
                                        ),
                                        Map.of(
                                                "time", "8:30",
                                                "entries", List.of(
                                                        Map.of(
                                                                "content", "Test News Entry 3",
                                                                "severity", 1
                                                        )
                                                )
                                        )
                                )
                        )
                );

        this.mockMvc.perform(post("/api/v1/news/create").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.date", is("2023-01-19")));
    }


    //  ----------------- postFetchNews -----------------

    @Test
    public void test_postFetchNews_failure() throws Exception {
        Mockito.when(this.marketNewsService.fetchMarketNews()).thenReturn(false);
        this.mockMvc.perform(post("/api/v1/news/fetch-news").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(false)));
    }

    @Test
    public void test_postFetchNews_success() throws Exception {
        Mockito.when(this.marketNewsService.fetchMarketNews()).thenReturn(true);
        this.mockMvc.perform(post("/api/v1/news/fetch-news").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }


    //  ----------------- putUpdateNews -----------------

    @Test
    public void test_putUpdateNews_badJsonIntegrity() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("uid", List.of("test"));

        this.mockMvc.perform(put("/api/v1/news/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_putUpdateNews_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("uid", List.of("test"));

        Map<String, Object> data =
                Map.of(
                        "marketNews",
                        Map.of(
                                "date", "2022-09-05",
                                "slots", List.of(
                                        Map.of(
                                                "time", "14:00",
                                                "entries", List.of(
                                                        Map.of(
                                                                "content", "Test News Entry 1",
                                                                "severity", 3
                                                        ),
                                                        Map.of(
                                                                "content", "Test News Entry 2",
                                                                "severity", 2
                                                        )
                                                )
                                        ),
                                        Map.of(
                                                "time", "8:30",
                                                "entries", List.of(
                                                        Map.of(
                                                                "content", "Test News Entry 3",
                                                                "severity", 1
                                                        )
                                                )
                                        )
                                )
                        )
                );

        this.mockMvc.perform(put("/api/v1/news/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.date", is("2023-01-19")));
    }


    //  ----------------- deleteMarketNews -----------------

    @Test
    public void test_deleteMarketNews_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("uid", List.of("test"));

        this.mockMvc.perform(delete("/api/v1/news/delete").params(map))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data", is(true)));
    }
}
