package com.stephenprizio.traderbuddy.models.records.reporting;

import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingRecord;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingRecordStatistics;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link TradingRecordStatistics}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradingRecordStatisticsTest {

    private final List<TradingRecord> ENTRIES = List.of(
            new TradingRecord(LocalDateTime.MIN, LocalDateTime.now().minusDays(10), 15.0, 10, 6, 4, 60, 102.68, 1.25,4.57,true, false),
            new TradingRecord(LocalDateTime.MIN, LocalDateTime.now().minusDays(6), 15.0, 7, 3, 4, 42, 96.36, 1.17,1.89,true, false)
    );


    //  ----------------- getTotalNumberOfTrades -----------------

    @Test
    public void test_getTotalNumberOfTrades_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getTotalNumberOfTrades())
                .isEqualTo(17);
    }


    //  ----------------- getTotalNumberOfWinningTrades -----------------

    @Test
    public void test_getTotalNumberOfWinningTrades_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getTotalNumberOfWinningTrades())
                .isEqualTo(9);
    }


    //  ----------------- getTotalNumberOfLosingTrades -----------------

    @Test
    public void test_getTotalNumberOfLosingTrades_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getTotalNumberOfLosingTrades())
                .isEqualTo(8);
    }


    //  ----------------- getTradingRate -----------------

    @Test
    public void test_getTradingRate_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getTradingRate())
                .isEqualTo(8.5);
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
