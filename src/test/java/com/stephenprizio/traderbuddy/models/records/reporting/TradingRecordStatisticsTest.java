package com.stephenprizio.traderbuddy.models.records.reporting;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingRecord;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingRecordStatistics;
import org.junit.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link TradingRecordStatistics}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradingRecordStatisticsTest extends AbstractGenericTest {

    private final List<TradingRecord> ENTRIES = generateTradingSummary().records();


    //  ----------------- getTotalNumberOfTrades -----------------

    @Test
    public void test_getTotalNumberOfTrades_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getTotalNumberOfTrades())
                .isEqualTo(2);
    }


    //  ----------------- getTotalNumberOfWinningTrades -----------------

    @Test
    public void test_getTotalNumberOfWinningTrades_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getTotalNumberOfWinningTrades())
                .isEqualTo(1);
    }


    //  ----------------- getTotalNumberOfLosingTrades -----------------

    @Test
    public void test_getTotalNumberOfLosingTrades_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getTotalNumberOfLosingTrades())
                .isEqualTo(1);
    }


    //  ----------------- getTradingRate -----------------

    @Test
    public void test_getTradingRate_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getTradingRate())
                .isEqualTo(2.0);
    }


    //  ----------------- getAverageWinPercentage -----------------

    @Test
    public void test_getAverageWinPercentage_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getAverageWinPercentage())
                .isEqualTo(50);
    }


    //  ----------------- getNetProfit -----------------

    @Test
    public void test_getNetProfit_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getNetProfit())
                .isEqualTo(10.35);
    }


    //  ----------------- getAverageProfitPercentage -----------------

    @Test
    public void test_getAverageProfitPercentage_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getAverageProfitPercentage())
                .isEqualTo(1.25);
    }


    //  ----------------- getSurplus -----------------

    @Test
    public void test_getSurplus_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getSurplus())
                .isEqualTo(11.11);
    }


    //  ----------------- getAverageWinSize -----------------

    @Test
    public void test_getAverageWinSize_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getAverageWinSize())
                .isEqualTo(0.75);
    }


    //  ----------------- getLargestWinSize -----------------

    @Test
    public void test_getLargestWinSize_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getLargestWinSize())
                .isEqualTo(0.75);
    }


    //  ----------------- getAverageLossSize -----------------

    @Test
    public void test_getAverageLossSize_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getAverageLossSize())
                .isEqualTo(0.75);
    }


    //  ----------------- getLargestLossSize -----------------

    @Test
    public void test_getLargestLossSize_success() {
        assertThat(new TradingRecordStatistics(ENTRIES).getLargestLossSize())
                .isEqualTo(0.75);
    }
}
