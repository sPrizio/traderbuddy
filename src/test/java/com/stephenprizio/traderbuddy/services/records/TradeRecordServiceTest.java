package com.stephenprizio.traderbuddy.services.records;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import com.stephenprizio.traderbuddy.exceptions.validation.IllegalParameterException;
import com.stephenprizio.traderbuddy.models.entities.trades.Trade;
import com.stephenprizio.traderbuddy.repositories.records.TradeRecordRepository;
import com.stephenprizio.traderbuddy.services.trades.TradeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Level;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.system.OutputCaptureExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link TradeRecordService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@ExtendWith(OutputCaptureExtension.class)
public class TradeRecordServiceTest extends AbstractGenericTest {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(TradeRecordService.class);

    private final Trade TEST_TRADE_1 = generateTestBuyTrade();
    private final Trade TEST_TRADE_2 = generateTestSellTrade();

    @MockBean
    private TradeRecordRepository tradeRecordRepository;

    @MockBean
    private TradeService tradeService;

    @Autowired
    private TradeRecordService tradeRecordService;

    @Before
    public void setUp() {
        Mockito.when(this.tradeRecordRepository.findTradingRecordByStartDateAndEndDateAndAggregateInterval(any(), any(), any())).thenReturn(generateTestTradeRecord());
        Mockito.when(this.tradeService.findAllTradesWithinDate(any(), any(), any())).thenReturn(List.of(TEST_TRADE_1, TEST_TRADE_2));
        Mockito.when(this.tradeService.findTradesByProcessed(any())).thenReturn(List.of(TEST_TRADE_2));
    }


    //  ----------------- findTradingRecordForStartDateAndEndDateAndInterval -----------------

    @Test
    public void test_findTradingRecordForStartDateAndEndDateAndInterval_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.findTradingRecordForStartDateAndEndDateAndInterval(null, LocalDate.MAX, AggregateInterval.DAILY))
                .withMessage("start date cannot be null");

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.findTradingRecordForStartDateAndEndDateAndInterval(LocalDate.MIN, null, AggregateInterval.DAILY))
                .withMessage("end date cannot be null");

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.findTradingRecordForStartDateAndEndDateAndInterval(LocalDate.MIN, LocalDate.MAX, null))
                .withMessage("interval cannot be null");

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradeRecordService.findTradingRecordForStartDateAndEndDateAndInterval(LocalDate.MAX, LocalDate.MIN, AggregateInterval.DAILY))
                .withMessage("start date was after end date or vice versa");
    }

    @Test
    public void test_findTradingRecordForStartDateAndEndDateAndInterval_success() {
        assertThat(this.tradeRecordService.findTradingRecordForStartDateAndEndDateAndInterval(LocalDate.MIN, LocalDate.MAX, AggregateInterval.DAILY))
                .isNotEmpty();
    }


    //  ----------------- generateTradeRecord -----------------

    @Test
    public void test_generateTradeRecord_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.generateTradeRecord(null, AggregateInterval.DAILY))
                .withMessage("date cannot be null");

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.generateTradeRecord(LocalDate.MIN, null))
                .withMessage("interval cannot be null");
    }

    @Test
    public void test_generateTradeRecord_daily_success() {
        assertThat(this.tradeRecordService.generateTradeRecord(LocalDate.of(2022, 8, 24), AggregateInterval.DAILY))
                .isNotNull()
                .extracting("endDate", "numberOfTrades")
                .containsExactly(LocalDate.of(2022, 8, 25), 2);
    }

    @Test
    public void test_generateTradeRecord_weekly_success() {
        assertThat(this.tradeRecordService.generateTradeRecord(LocalDate.of(2022, 8, 24), AggregateInterval.WEEKLY))
                .isNotNull()
                .extracting("endDate", "numberOfTrades")
                .containsExactly(LocalDate.of(2022, 8, 28), 2);
    }

    @Test
    public void test_generateTradeRecord_monthly_success() {
        assertThat(this.tradeRecordService.generateTradeRecord(LocalDate.of(2022, 8, 24), AggregateInterval.MONTHLY))
                .isNotNull()
                .extracting("endDate", "numberOfTrades")
                .containsExactly(LocalDate.of(2022, 8, 31), 2);
    }

    @Test
    public void test_generateTradeRecord_yearly_success() {
        assertThat(this.tradeRecordService.generateTradeRecord(LocalDate.of(2022, 8, 24), AggregateInterval.YEARLY))
                .isNotNull()
                .extracting("endDate", "numberOfTrades")
                .containsExactly(LocalDate.of(2022, 12, 31), 2);
    }


    //  ----------------- processTrades -----------------

    @Test
    public void test_processTrades_success() {

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        LOGGER.addAppender(listAppender);

        this.tradeRecordService.processTrades();

        assertThat(listAppender.list)
                .hasSize(4)
                .first()
                .extracting("formattedMessage", "level")
                .containsExactly("Processed 1 DAILY records", Level.INFO);

        listAppender.stop();
    }
}
