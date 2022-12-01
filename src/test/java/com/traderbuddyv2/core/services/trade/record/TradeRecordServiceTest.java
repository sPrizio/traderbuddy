package com.traderbuddyv2.core.services.trade.record;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import com.traderbuddyv2.core.models.entities.security.User;
import com.traderbuddyv2.core.models.entities.trade.Trade;
import com.traderbuddyv2.core.models.entities.trade.record.TradeRecord;
import com.traderbuddyv2.core.repositories.trade.record.TradeRecordRepository;
import com.traderbuddyv2.core.repositories.trade.record.TradeRecordStatisticsRepository;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import com.traderbuddyv2.core.services.trade.TradeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.*;

/**
 * Testing class for {@link TradeRecordService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TradeRecordServiceTest extends AbstractGenericTest {

    private static final Logger LOGGER = (Logger) LoggerFactory.getLogger(TradeRecordService.class);

    private final User user = generateTestUser();

    private final Trade trade = generateTestBuyTrade();

    @MockBean
    private MathService mathService;

    @MockBean
    private TradeRecordRepository tradeRecordRepository;

    @MockBean
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @MockBean
    private TradeService tradeService;

    @MockBean
    private TradeRecordStatisticsRepository tradeRecordStatisticsRepository;

    @Autowired
    private TradeRecordService tradeRecordService;

    @Before
    public void setUp() {
        Mockito.when(this.tradeRecordRepository.findTradeRecordByStartDateAndEndDateAndAggregateIntervalAndAccount(any(), any(), any(), any())).thenReturn(generateTestTradeRecord());
        Mockito.when(this.traderBuddyUserDetailsService.getCurrentUser()).thenReturn(user);
        Mockito.when(this.mathService.getDouble(10.35)).thenReturn(10.35);
        Mockito.when(this.tradeService.findAllTradesWithinTimespan(any(), any(), anyBoolean())).thenReturn(List.of(generateTestBuyTrade(), generateTestSellTrade()));
        Mockito.when(this.tradeService.findTradesByProcessed(false)).thenReturn(List.of(generateTestBuyTrade(), generateTestSellTrade()));
        Mockito.when(this.tradeRecordRepository.save(any())).thenAnswer(invocation -> {
            TradeRecord record = invocation.getArgument(0, TradeRecord.class);
            TradeRecord temp = generateTestTradeRecord();

            if (record.getStartDate().equals(LocalDate.of(2022, 8, 24))) {
                temp.setStartDate(LocalDate.of(2022, 8, 24));
                temp.setEndDate(LocalDate.of(2022, 8, 25));
            } else if (record.getStartDate().equals(LocalDate.of(2022, 8, 22))) {
                temp.setStartDate(LocalDate.of(2022, 8, 22));
                temp.setEndDate(LocalDate.of(2022, 8, 28));
            } else if (record.getStartDate().equals(LocalDate.of(2022, 8, 1))) {
                temp.setStartDate(LocalDate.of(2022, 8, 1));
                temp.setEndDate(LocalDate.of(2022, 8, 31));
            } else if (record.getStartDate().equals(LocalDate.of(2022, 1, 1))) {
                temp.setStartDate(LocalDate.of(2022, 1, 1));
                temp.setEndDate(LocalDate.of(2022, 12, 31));
            }

            temp.setStatistics(generateTestTradeRecordStatistics());
            return temp;
        });
        Mockito.when(this.mathService.add(1000.0, 10.35)).thenReturn(1010.35);
        Mockito.when(this.tradeRecordRepository.findRecentHistory(anyInt(), any(), any())).thenReturn(List.of(generateTestTradeRecord()));
        Mockito.when(this.tradeRecordRepository.findHistory(any(), any(), any(), any())).thenReturn(List.of(generateTestTradeRecord()));
        Mockito.when(this.tradeService.findTradeByTradeId(anyString())).thenReturn(Optional.of(trade));
    }


    //  ----------------- findTradeRecordForStartDateAndEndDateAndInterval -----------------

    @Test
    public void test_findTradingRecordForStartDateAndEndDateAndInterval_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.findTradeRecordForStartDateAndEndDateAndInterval(null, LocalDate.MAX, AggregateInterval.MONTHLY))
                .withMessage(CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.findTradeRecordForStartDateAndEndDateAndInterval(LocalDate.MIN, null, AggregateInterval.MONTHLY))
                .withMessage(CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.findTradeRecordForStartDateAndEndDateAndInterval(LocalDate.MIN, LocalDate.MAX, null))
                .withMessage(CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradeRecordService.findTradeRecordForStartDateAndEndDateAndInterval(LocalDate.MAX, LocalDate.MIN, AggregateInterval.MONTHLY))
                .withMessage(CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    public void test_findTradingRecordForStartDateAndEndDateAndInterval_success() {
        assertThat(this.tradeRecordService.findTradeRecordForStartDateAndEndDateAndInterval(LocalDate.MIN, LocalDate.MAX, AggregateInterval.DAILY))
                .isNotEmpty();
    }


    //  ----------------- findRecentHistory -----------------

    @Test
    public void test_findRecentHistory_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.findRecentHistory(1, null))
                .withMessage(CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);
    }

    @Test
    public void test_findRecentHistory_success() {
        assertThat(this.tradeRecordService.findRecentHistory(1, AggregateInterval.DAILY))
                .isNotEmpty();
    }


    //  ----------------- findHistory -----------------

    @Test
    public void test_findHistory_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.findHistory(null, LocalDate.MAX, AggregateInterval.MONTHLY))
                .withMessage(CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.findHistory(LocalDate.MIN, null, AggregateInterval.MONTHLY))
                .withMessage(CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.findHistory(LocalDate.MIN, LocalDate.MAX, null))
                .withMessage(CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradeRecordService.findHistory(LocalDate.MAX, LocalDate.MIN, AggregateInterval.MONTHLY))
                .withMessage(CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    public void test_findHistory_success() {
        assertThat(this.tradeRecordService.findHistory(LocalDate.MIN, LocalDate.MAX, AggregateInterval.DAILY))
                .isNotEmpty();
    }


    //  ----------------- findActiveMonths -----------------

    @Test
    public void test_findActiveMonths_missingParams() {
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> this.tradeRecordService.findActiveMonths(1000000000))
                .withMessage("The given year 1000000000 was higher than the maximum allowable 999999999");
    }

    @Test
    public void test_findActiveMonths_success() {
        assertThat(this.tradeRecordService.findActiveMonths(2022))
                .isNotEmpty();
    }


    //  ----------------- findTradeRecordForTrade -----------------

    @Test
    public void test_findTradeRecordForTrade_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.findTradeRecordForTrade(null, AggregateInterval.DAILY))
                .withMessage("trade cannot be null");
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradeRecordService.findTradeRecordForTrade(trade, null))
                .withMessage(CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);
    }

    @Test
    public void test_findTradeRecordForTrade_success() {
        assertThat(this.tradeRecordService.findTradeRecordForTrade(trade, AggregateInterval.DAILY))
                .isNotEmpty();
    }


    //  ----------------- processDisregardedTrade -----------------

    @Test
    public void test_processDisregardedTrade_success() {

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        LOGGER.addAppender(listAppender);

        this.tradeRecordService.processDisregardedTrade("test");

        assertThat(listAppender.list)
                .hasSize(1)
                .first()
                .extracting("formattedMessage", "level")
                .containsExactly("Trade processing completed without issue", Level.INFO);

        listAppender.stop();
    }


    //  ----------------- processTrades -----------------

    @Test
    public void test_processTrades_success() {

        ListAppender<ILoggingEvent> listAppender = new ListAppender<>();
        listAppender.start();
        LOGGER.addAppender(listAppender);

        this.tradeRecordService.processTrades();

        assertThat(listAppender.list)
                .hasSize(1)
                .first()
                .extracting("formattedMessage", "level")
                .containsExactly("Trade processing completed without issue", Level.INFO);

        listAppender.stop();
    }
}
