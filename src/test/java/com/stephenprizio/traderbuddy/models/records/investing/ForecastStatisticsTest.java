package com.stephenprizio.traderbuddy.models.records.investing;

import org.junit.Test;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link ForecastStatistics}
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
public class ForecastStatisticsTest {

    private final List<ForecastEntry> ENTRIES =
            List.of(
                    new ForecastEntry(LocalDate.MIN, LocalDate.now(), 1.0, 1.0, 1226.0, 350.0, 125.0, 15.0),
                    new ForecastEntry(LocalDate.now(), LocalDate.MAX, 2.0, 3.0, 1228.0, 0.0, 0.0, 15.0)
            );


    //  ----------------- getTotalDeposits -----------------

    @Test
    public void test_getTotalDeposits_success() {
        assertThat(new ForecastStatistics(1000.0, ENTRIES).getTotalDeposits())
                .isEqualTo(350.0);
    }


    //  ----------------- getTotalEarnings -----------------

    @Test
    public void test_getTotalEarnings_success() {
        assertThat(new ForecastStatistics(1000.0, ENTRIES).getTotalEarnings())
                .isEqualTo(3.0);
    }


    //  ----------------- getTotalNetEarnings -----------------

    @Test
    public void test_getTotalNetEarnings_success() {
        assertThat(new ForecastStatistics(1000.0, ENTRIES).getTotalNetEarnings())
                .isEqualTo(353.0);
    }


    //  ----------------- getTotalWithdrawals -----------------

    @Test
    public void test_getTotalWithdrawals_success() {
        assertThat(new ForecastStatistics(1000.0, ENTRIES).getTotalWithdrawals())
                .isEqualTo(125.0);
    }


    //  ----------------- getTotalBalance -----------------

    @Test
    public void test_getTotalBalance_success_no_entries() {
        assertThat(new ForecastStatistics(1000.0, Collections.emptyList()).getTotalBalance())
                .isEqualTo(0.0);
    }

    @Test
    public void test_getTotalBalance_success() {
        assertThat(new ForecastStatistics(1000.0, ENTRIES).getTotalBalance())
                .isEqualTo(1228.0);
    }
}
