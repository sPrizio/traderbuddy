package com.traderbuddyv2.api.controllers.system;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traderbuddyv2.api.constants.ApiConstants;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link SystemController}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class SystemControllerTest {

    @Autowired
    private MockMvc mockMvc;


    //  ----------------- getCurrentUser -----------------

    @Test
    public void test_postCreateRetrospective_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post("/api/v1/system/contact").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_postCreateRetrospective_success() throws Exception {

        Map<String, Object> data =
                Map.of(
                        "contact",
                        Map.of(
                                "name", "Test User",
                                "email", "test@email.com",
                                "subject", "Greetings",
                                "message", "Hello There, General Kenobi"
                        )
                );

        this.mockMvc.perform(post("/api/v1/system/contact").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success", is(true)));
    }
}
