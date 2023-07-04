package com.traderbuddyv2.api.controllers.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.constants.ApiConstants;
import com.traderbuddyv2.api.converters.account.AccountDTOConverter;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import com.traderbuddyv2.core.services.security.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Testing class for {@link UserApiController}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@RunWith(SpringRunner.class)
public class UserApiControllerTest extends AbstractGenericTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @MockBean
    private AccountDTOConverter accountDTOConverter;

    @MockBean
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @MockBean
    private UserService userService;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.traderBuddyUserDetailsService.getCurrentUser()).thenReturn(generateTestUser());
        Mockito.when(this.accountDTOConverter.convert(any())).thenReturn(generateTestAccountDTO());
        Mockito.when(this.userService.createUser(anyMap())).thenReturn(generateTestUser());
        Mockito.when(this.userService.updateUser(anyString(), anyMap())).thenReturn(generateTestUser());
    }


    //  ----------------- getCurrentUser -----------------

    @Test
    public void test_getCurrentUser_success() throws Exception {
        this.mockMvc.perform(get("/api/v1/user/current-user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.lastName", is("Test")));
    }


    //  ----------------- postCreateUser -----------------

    @Test
    public void test_postCreateUser_badJsonIntegrity() throws Exception {
        this.mockMvc.perform(post("/api/v1/user/create").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_postCreateUser_success() throws Exception {

        Map<String, Object> temp = new HashMap<>();
        temp.put("email", "2022-09-05");
        temp.put("password", "2022-09-11");
        temp.put("lastName", "Prizio");
        temp.put("firstName", "Stephen");
        temp.put("username", "s.prizio");
        temp.put("phoneType", "MOBILE");
        temp.put("countryCode", "1");
        temp.put("phoneNumber", "5149411025");
        temp.put("country", "CAN");
        temp.put("townCity", "Montreal");
        temp.put("timeZoneOffset", "America/Toronto");

        Map<String, Object> data = new java.util.HashMap<>(Map.of("user", temp));

        this.mockMvc.perform(post("/api/v1/user/create").contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName", is("Stephen")));
    }


    //  ----------------- putUpdateUser -----------------

    @Test
    public void test_putUpdateRetrospective_badJsonIntegrity() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("email", List.of("test@email.com"));

        this.mockMvc.perform(put("/api/v1/user/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(Map.of("hello", "world"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", containsString(ApiConstants.CLIENT_ERROR_DEFAULT_MESSAGE)));
    }

    @Test
    public void test_putUpdateRetrospective_success() throws Exception {

        MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
        map.put("email", List.of("test@email.com"));

        Map<String, Object> temp = new HashMap<>();
        temp.put("email", "2022-09-05");
        temp.put("password", "2022-09-11");
        temp.put("lastName", "Prizio");
        temp.put("firstName", "Stephen");
        temp.put("username", "s.prizio");
        temp.put("phoneType", "MOBILE");
        temp.put("countryCode", "1");
        temp.put("phoneNumber", "5149411025");
        temp.put("country", "CAN");
        temp.put("townCity", "Montreal");
        temp.put("timeZoneOffset", "America/Toronto");

        Map<String, Object> data = new java.util.HashMap<>(Map.of("user", temp));

        this.mockMvc.perform(put("/api/v1/user/update").params(map).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(data)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.firstName", is("Stephen")));
    }
}
