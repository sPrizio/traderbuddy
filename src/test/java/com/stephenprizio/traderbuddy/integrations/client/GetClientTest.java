package com.stephenprizio.traderbuddy.integrations.client;

import com.stephenprizio.traderbuddy.integrations.exceptions.IntegrationException;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link GetClient}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class GetClientTest {


    //  ----------------- doGet -----------------

    @Test
    public void test_doGet_failure() {
        assertThatExceptionOfType(IntegrationException.class)
                .isThrownBy(() -> GetClient.doGet(null, null))
                .withMessageContaining("There was an error connecting to");
    }

    @Test
    public void test_doGet_success() {
        assertThat(GetClient.doGet("https://www.google.ca", null))
                .isNotEmpty();
    }
}
