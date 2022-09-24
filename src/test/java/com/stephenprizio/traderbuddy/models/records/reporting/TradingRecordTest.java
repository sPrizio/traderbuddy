package com.stephenprizio.traderbuddy.models.records.reporting;

import com.stephenprizio.traderbuddy.models.records.reporting.trades.TradingRecord;
import org.junit.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link TradingRecord}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TradingRecordTest {


    //  ----------------- isEmpty -----------------

    @Test
    public void test_isEmpty_success_empty() {
        TradingRecord tradingRecord = new TradingRecord(null, null, 0.0, 0, 0, 0.0, 0.0, 0.0, true,true);
        assertThat(tradingRecord.isEmpty())
                .isFalse();
    }

    @Test
    public void test_isEmpty_success_weekend() {
        TradingRecord tradingRecord = new TradingRecord(LocalDate.of(2022, 9, 17).atStartOfDay(), LocalDate.of(2022, 9, 18).atStartOfDay(), 0.0, 1, 0, 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.isEmpty())
                .isTrue();
    }

    @Test
    public void test_isEmpty_success_no_trades() {
        TradingRecord tradingRecord = new TradingRecord(LocalDateTime.now(), LocalDateTime.now(), 0.0, 0, 0, 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.isEmpty())
                .isTrue();
    }


    //  ----------------- isWeekend -----------------

    @Test
    public void test_isWeekend_success_weekday() {
        TradingRecord tradingRecord = new TradingRecord(LocalDate.of(2022, 9, 14).atStartOfDay(), LocalDate.of(2022, 9, 15).atStartOfDay(), 0.0, 1, 0, 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.isWeekend())
                .isFalse();
    }

    @Test
    public void test_isWeekend_success_weekend() {
        TradingRecord tradingRecord = new TradingRecord(LocalDate.of(2022, 9, 17).atStartOfDay(), LocalDate.of(2022, 9, 18).atStartOfDay(), 0.0, 1, 0, 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.isWeekend())
                .isTrue();
    }


    //  ----------------- getActive -----------------

    @Test
    public void test_getActive_success_notActive() {
        TradingRecord tradingRecord = new TradingRecord(LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(6), 0.0, 1, 0, 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.getActive())
                .isFalse();
    }

    @Test
    public void test_getActive_success() {
        TradingRecord tradingRecord = new TradingRecord(LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(6), 0.0, 1, 0, 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.getActive())
                .isTrue();
    }


    //  ----------------- isCompletedSession -----------------

    @Test
    public void test_isCompletedSession_success_false() {
        TradingRecord tradingRecord = new TradingRecord(LocalDateTime.now().minusDays(2), LocalDateTime.now().plusDays(6), 0.0, 1, 0, 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.isCompletedSession())
                .isFalse();
    }

    @Test
    public void test_isCompletedSession_success() {
        TradingRecord tradingRecord = new TradingRecord(LocalDateTime.now().minusDays(10), LocalDateTime.now().minusDays(6), 0.0, 1, 0, 0.0, 0.0, 0.0, false,true);
        assertThat(tradingRecord.isCompletedSession())
                .isTrue();
    }
}
