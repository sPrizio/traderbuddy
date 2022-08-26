package com.stephenprizio.traderbuddy.services.summary;

import com.stephenprizio.traderbuddy.AbstractTraderBuddyTest;
import com.stephenprizio.traderbuddy.enums.TradesSummaryInterval;
import com.stephenprizio.traderbuddy.exceptions.validation.IllegalParameterException;
import com.stephenprizio.traderbuddy.models.entities.Trade;
import com.stephenprizio.traderbuddy.repositories.TradeRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link TradesSummaryService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@DataJpaTest
@RunWith(SpringRunner.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TradeSummaryServiceWithDataTest extends AbstractTraderBuddyTest {

    private static final LocalDateTime TEST_DAY1 = LocalDate.of(2022, 8, 24).atStartOfDay();
    private static final LocalDateTime TEST_DAY2 = LocalDate.of(2022, 9, 28).atStartOfDay();

    private final List<Trade> TRADES = generateTrades(20);

    private TradesSummaryService tradesSummaryService;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private TradeRepository tradeRepository;

    @Before
    public void setUp() {
        this.tradesSummaryService = new TradesSummaryService(this.tradeRepository);
        TRADES.forEach(trade -> this.entityManager.persist(trade));
        this.entityManager.flush();
    }


    //  ----------------- getReportOfSummariesForTimeSpan -----------------

    @Test
    public void test_getReportOfSummariesForTimeSpan_missingParamStart() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradesSummaryService.getReportOfSummariesForTimeSpan(null, LocalDateTime.MAX, TradesSummaryInterval.DAILY))
                .withMessage("startDate cannot be null");
    }

    @Test
    public void test_getReportOfSummariesForTimeSpan_missingParamEnd() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradesSummaryService.getReportOfSummariesForTimeSpan(LocalDateTime.MIN, null, TradesSummaryInterval.DAILY))
                .withMessage("endDate cannot be null");
    }

    @Test
    public void test_getReportOfSummariesForTimeSpan_missingParamInterval() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.tradesSummaryService.getReportOfSummariesForTimeSpan(LocalDateTime.MIN, LocalDateTime.MAX, null))
                .withMessage("interval cannot be null");
    }

    @Test
    public void test_getReportOfSummariesForTimeSpan_invalidTimespan() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.tradesSummaryService.getReportOfSummariesForTimeSpan(LocalDateTime.MAX, LocalDateTime.MIN, TradesSummaryInterval.DAILY))
                .withMessage("startDate was after endDate or vice versa");
    }

    @Test
    public void test_getReportOfSummariesForTimeSpan_success_daily() {
        assertThat(this.tradesSummaryService.getReportOfSummariesForTimeSpan(TEST_DAY1, TEST_DAY2, TradesSummaryInterval.DAILY))
                .isNotEmpty()
                .element(23)
                .extracting("netProfit", "numberOfTrades")
                .contains(TRADES.get(0).getNetProfit(), 1);
    }

    @Test
    public void test_getReportOfSummariesForTimeSpan_success_weekly() {
        BigDecimal compare = BigDecimal.ZERO;
        for (Trade trade : TRADES.subList(0, 5)) {
            compare = compare.add(BigDecimal.valueOf(trade.getNetProfit()).setScale(2, RoundingMode.HALF_EVEN));
        }

        assertThat(this.tradesSummaryService.getReportOfSummariesForTimeSpan(TEST_DAY1, TEST_DAY2, TradesSummaryInterval.WEEKLY))
                .isNotEmpty()
                .element(3)
                .extracting("netProfit", "numberOfTrades")
                .contains(compare.doubleValue(), 5);
    }

    @Test
    public void test_getReportOfSummariesForTimeSpan_success_monthly() {
        BigDecimal compare = BigDecimal.ZERO;
        for (Trade trade : TRADES.subList(0, 8)) {
            compare = compare.add(BigDecimal.valueOf(trade.getNetProfit()).setScale(2, RoundingMode.HALF_EVEN));
        }

        assertThat(this.tradesSummaryService.getReportOfSummariesForTimeSpan(TEST_DAY1, TEST_DAY2, TradesSummaryInterval.MONTHLY))
                .isNotEmpty()
                .first()
                .extracting("netProfit", "numberOfTrades")
                .contains(compare.doubleValue(), 8);
    }

    @Test
    public void test_getReportOfSummariesForTimeSpan_success_yearly() {
        BigDecimal compare = BigDecimal.ZERO;
        for (Trade trade : TRADES) {
            compare = compare.add(BigDecimal.valueOf(trade.getNetProfit()).setScale(2, RoundingMode.HALF_EVEN));
        }

        assertThat(this.tradesSummaryService.getReportOfSummariesForTimeSpan(TEST_DAY1, TEST_DAY2, TradesSummaryInterval.YEARLY))
                .isNotEmpty()
                .first()
                .extracting("netProfit", "numberOfTrades")
                .contains(compare.doubleValue(), 20);
    }
}
