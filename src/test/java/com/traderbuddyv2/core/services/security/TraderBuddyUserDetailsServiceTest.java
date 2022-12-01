package com.traderbuddyv2.core.services.security;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.exceptions.system.AnonymousUserException;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Testing class for {@link TraderBuddyUserDetailsService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TraderBuddyUserDetailsServiceTest extends AbstractGenericTest {

    @MockBean
    private UserService userService;

    @Autowired
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @Before
    public void setUp() {
        Mockito.when(this.userService.findUserByUsername("test")).thenReturn(Optional.of(generateTestUser()));
        Mockito.when(this.userService.findUserByEmail("test@email.com")).thenReturn(Optional.of(generateTestUser()));
        Mockito.when(this.userService.findUser(anyString())).thenReturn(Optional.of(generateTestUser()));
    }


    //  ----------------- loadUserByUsername -----------------

    @Test
    public void test_loadUserByUsername_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.traderBuddyUserDetailsService.loadUserByUsername(null))
                .withMessage("username cannot be null");
    }

    @Test
    public void test_loadUserByUsername_badCredentials() {
        assertThatExceptionOfType(BadCredentialsException.class)
                .isThrownBy(() -> this.traderBuddyUserDetailsService.loadUserByUsername("badCredentials"))
                .withMessage("No username or email was found for value badCredentials");
    }

    @Test
    public void test_loadUserByUsername_success_email() {
        assertThat(this.traderBuddyUserDetailsService.loadUserByUsername("test"))
                .isNotNull();
    }

    @Test
    public void test_loadUserByUsername_success_username() {
        assertThat(this.traderBuddyUserDetailsService.loadUserByUsername("test@email.com"))
                .isNotNull();
    }


    //  ----------------- getCurrentUser -----------------

    @Test
    @WithMockUser(username = "test")
    public void test_getCurrentUser_success() {
        assertThat(this.traderBuddyUserDetailsService.getCurrentUser())
                .isNotNull()
                .extracting("username")
                .isEqualTo("s.prizio");
    }
}
