package com.stephenprizio.traderbuddy.models.records.reporting;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingRecord;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link TradingRecord}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradingRecordTest extends AbstractGenericTest {


    //  ----------------- isEmpty -----------------

    @Test
    public void test_isEmpty_success_empty() {
        TradingRecord tradingRecord = new TradingRecord(List.of(), null, null, 0.0, 0.0, 0.0, true,true);
        assertThat(tradingRecord.isEmpty())
                .isFalse();
    }

    @Test
    public void test_isEmpty_success_weekend() {
        TradingRecord tradingRecord = new TradingRecord(List.of(), LocalDate.of(2022, 9, 17).atStartOfDay(), LocalDate.of(2022, 9, 18).atStartOfDay(), 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.isEmpty())
                .isTrue();
    }

    @Test
    public void test_isEmpty_success_no_trades() {
        TradingRecord tradingRecord = new TradingRecord(List.of(), LocalDateTime.now(), LocalDateTime.now(), 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.isEmpty())
                .isTrue();
    }


    //  ----------------- isEmpty -----------------

    @Test
    public void test_isNotEmpty_success_empty() {
        TradingRecord tradingRecord = new TradingRecord(List.of(generateTestBuyTrade(), generateTestSellTrade()), LocalDate.of(2022, 9, 14).atStartOfDay(), LocalDate.of(2022, 9, 15).atStartOfDay(), 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.isNotEmpty())
                .isTrue();
    }


    //  ----------------- isWeekend -----------------

    @Test
    public void test_isWeekend_success_weekday() {
        TradingRecord tradingRecord = new TradingRecord(List.of(), LocalDate.of(2022, 9, 14).atStartOfDay(), LocalDate.of(2022, 9, 15).atStartOfDay(), 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.isWeekend())
                .isFalse();
    }

    @Test
    public void test_isWeekend_success_weekend() {
        TradingRecord tradingRecord = new TradingRecord(List.of(), LocalDate.of(2022, 9, 17).atStartOfDay(), LocalDate.of(2022, 9, 18).atStartOfDay(), 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.isWeekend())
                .isTrue();
    }


    //  ----------------- getActive -----------------

    @Test
    public void test_getActive_success_notActive() {
        TradingRecord tradingRecord = new TradingRecord(List.of(), LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(6), 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.getActive())
                .isFalse();
    }

    @Test
    public void test_getActive_success() {
        TradingRecord tradingRecord = new TradingRecord(List.of(), LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(6), 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.getActive())
                .isTrue();
    }


    //  ----------------- isCompletedSession -----------------

    @Test
    public void test_isCompletedSession_success_false() {
        TradingRecord tradingRecord = new TradingRecord(List.of(), LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(6), 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.isCompletedSession())
                .isFalse();
    }

    @Test
    public void test_isCompletedSession_success() {
        TradingRecord tradingRecord = new TradingRecord(List.of(), LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(6), 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.isCompletedSession())
                .isTrue();
    }


    //  ----------------- getTotalNumberOfTrades -----------------

    @Test
    public void test_getTotalNumberOfTrades_success_empty() {
        TradingRecord tradingRecord = new TradingRecord(List.of(), LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(6), 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.getTotalNumberOfTrades())
                .isZero();
    }

    @Test
    public void test_getTotalNumberOfTrades_success() {
        assertThat(generateTradingSummary().records().get(0).getTotalNumberOfTrades())
                .isEqualTo(2);
    }

    //  ----------------- getTotalNumberOfWinningTrades -----------------

    @Test
    public void test_getTotalNumberOfWinningTrades_success_empty() {
        TradingRecord tradingRecord = new TradingRecord(List.of(), LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(6), 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.getTotalNumberOfWinningTrades())
                .isZero();
    }

    @Test
    public void test_getTotalNumberOfWinningTrades_success() {
        assertThat(generateTradingSummary().records().get(0).getTotalNumberOfWinningTrades())
                .isEqualTo(1);
    }

    //  ----------------- getTotalNumberOfLosingTrades -----------------

    @Test
    public void test_getTotalNumberOfLosingTrades_success_empty() {
        TradingRecord tradingRecord = new TradingRecord(List.of(), LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(6), 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.getTotalNumberOfLosingTrades())
                .isZero();
    }

    @Test
    public void test_getTotalNumberOfLosingTrades_success() {
        assertThat(generateTradingSummary().records().get(0).getTotalNumberOfLosingTrades())
                .isEqualTo(1);
    }

    //  ----------------- getWinPercentage -----------------

    @Test
    public void test_getWinPercentage_success_empty() {
        TradingRecord tradingRecord = new TradingRecord(List.of(), LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(6), 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.getWinPercentage())
                .isZero();
    }

    @Test
    public void test_getWinPercentage_success() {
        assertThat(generateTradingSummary().records().get(0).getWinPercentage())
                .isEqualTo(50);
    }

    //  ----------------- getNetProfit -----------------

    @Test
    public void test_getNetProfit_success_empty() {
        TradingRecord tradingRecord = new TradingRecord(List.of(), LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(6), 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.getNetProfit())
                .isZero();
    }

    @Test
    public void test_getNetProfit_success() {
        assertThat(generateTradingSummary().records().get(0).getNetProfit())
                .isEqualTo(10.35);
    }
}
