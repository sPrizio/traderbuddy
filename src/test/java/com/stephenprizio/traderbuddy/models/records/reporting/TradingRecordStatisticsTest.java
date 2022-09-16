package com.stephenprizio.traderbuddy.models.records.reporting;

import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link TradingRecordStatistics}
 *
 * @author Stephen Prizio <a href="http://www.saprizio.com">www.saprizio.com</a>
 * @version 1.0
 */
public class TradingRecordStatisticsTest {

    private final List<TradingRecord> ENTRIES = List.of(
            new TradingRecord(LocalDateTime.MIN, LocalDateTime.now().minusDays(10), 15.0, 10, 60, 102.68, 1.25,4.57,true, false),
            new TradingRecord(LocalDateTime.MIN, LocalDateTime.now().minusDays(6), 15.0, 7, 42, 96.36, 1.17,1.89,true, false)
    );


    //  ----------------- getTotalNumberOfTrades -----------------

    @Test
    public void test_getTotalNumberOfTrades_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getTotalNumberOfTrades())
                .isEqualTo(17);
    }


    //  ----------------- getAverageWinPercentage -----------------

    @Test
    public void test_getAverageWinPercentage_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getAverageWinPercentage())
                .isEqualTo(51);
    }


    //  ----------------- getNetProfit -----------------

    @Test
    public void test_getNetProfit_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getNetProfit())
                .isEqualTo(199.04);
    }


    //  ----------------- getAverageProfitPercentage -----------------

    @Test
    public void test_getAverageProfitPercentage_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getAverageProfitPercentage())
                .isEqualTo(1.21);
    }


    //  ----------------- getSurplus -----------------

    @Test
    public void test_getSurplus_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getSurplus())
                .isEqualTo(6.46);
    }
}
