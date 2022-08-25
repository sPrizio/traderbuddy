package com.stephenprizio.traderbuddy.services.importing.impl;

import com.stephenprizio.traderbuddy.exceptions.importing.TradeImportFailureException;
import com.stephenprizio.traderbuddy.repositories.TradeRepository;
import org.assertj.core.groups.Tuple;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


/**
 * Testing class for {@link CMCTradesImportService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@DataJpaTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CMCTradesImportServiceTest {

    private CMCTradesImportService cmcTradesImportService;

    @Resource(name = "tradeRepository")
    private TradeRepository tradeRepository;


    //  METHODS

    @Before
    public void setUp() {
        this.cmcTradesImportService = new CMCTradesImportService(this.tradeRepository);
    }

    @Test
    @Order(1)
    public void test_importTrades_failure() {
        assertThatExceptionOfType(TradeImportFailureException.class)
                .isThrownBy(() -> this.cmcTradesImportService.importTrades("src/main/resources/testing/NotFound.csv", ";"))
                .withMessageContaining("The import process failed with reason");
    }

    @Test
    @Order(2)
    public void test_importTrades_success() {
        this.cmcTradesImportService.importTrades("classpath:testing/History.csv", ";");

        assertThat(this.tradeRepository.findAll())
                .hasSize(3)
                .extracting("tradeId", "lotSize", "tradeOpenTime", "tradeCloseTime", "openPrice", "closePrice", "netProfit")
                .contains(
                        Tuple.tuple("O5-77-5H7P05", 0.80, LocalDateTime.of(2022, 8, 24, 11, 23), LocalDateTime.of(2022, 8, 24, 11, 27), 12960.00, 12972.38, 12.78),
                        Tuple.tuple("O5-77-5H7MXX", 0.75, LocalDateTime.of(2022, 8, 24, 11, 13), LocalDateTime.of(2022, 8, 24, 11, 14), 12935.17, 12943.36, -8.0),
                        Tuple.tuple("1109841303", 0.0, LocalDateTime.of(2022, 8, 24, 11, 14), LocalDateTime.of(2022, 8, 24, 11, 14), 0.0, 0.0, 8.0)
                );
    }

    @Test
    @Order(3)
    public void testImportTrades_success_unchanged() {
        this.cmcTradesImportService.importTrades("classpath:testing/History.csv", ";");
        this.cmcTradesImportService.importTrades("classpath:testing/History.csv", ";");

        assertThat(this.tradeRepository.findAll())
                .hasSize(3);
    }
}
