package com.traderbuddyv2.core.services.security;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import com.traderbuddyv2.core.repositories.security.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link UserService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class UserServiceTest extends AbstractGenericTest {

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Before
    public void setUp() {
        Mockito.when(this.userRepository.findUserByEmail("test@email.com")).thenReturn(generateTestUser());
        Mockito.when(this.userRepository.findUserByUsername("test")).thenReturn(generateTestUser());
    }


    //  ----------------- findUserByUsername -----------------

    @Test
    public void test_findUserByUsername_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.userService.findUserByUsername(null))
                .withMessage("username cannot be null");
    }

    @Test
    public void test_findUserByUsername_success() {
        assertThat(this.userService.findUserByUsername("test"))
                .isNotEmpty();
    }


    //  ----------------- findUserByEmail -----------------

    @Test
    public void test_findUserByEmail_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.userService.findUserByEmail(null))
                .withMessage("email cannot be null");
    }

    @Test
    public void test_findUserByEmail_success() {
        assertThat(this.userService.findUserByEmail("test@email.com"))
                .isNotEmpty();
    }


    //  ----------------- findUser -----------------

    @Test
    public void test_findUser_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.userService.findUser(null))
                .withMessage("username/email cannot be null");
    }

    @Test
    public void test_findUser_success_username() {
        assertThat(this.userService.findUser("test"))
                .isNotEmpty();
    }

    @Test
    public void test_findUser_success_email() {
        assertThat(this.userService.findUser("test@email.com"))
                .isNotEmpty();
    }
}
