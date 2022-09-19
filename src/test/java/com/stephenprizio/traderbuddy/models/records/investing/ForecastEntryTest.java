package com.stephenprizio.traderbuddy.models.records.investing;

import org.junit.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link ForecastEntry}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class ForecastEntryTest {


    //  ----------------- getActive -----------------

    @Test
    public void test_getActive_success_false() {
        ForecastEntry entry = new ForecastEntry(LocalDate.now().minusDays(10), LocalDate.now().minusDays(6), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        assertThat(entry.getActive())
                .isFalse();
    }

    @Test
    public void test_getActive_success_true() {
        ForecastEntry entry = new ForecastEntry(LocalDate.now().minusDays(1), LocalDate.now().plusDays(5), 0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
        assertThat(entry.getActive())
                .isTrue();
    }
}
