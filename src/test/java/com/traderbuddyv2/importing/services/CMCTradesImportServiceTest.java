package com.traderbuddyv2.importing.services;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.models.entities.account.Account;
import com.traderbuddyv2.core.models.entities.security.User;
import com.traderbuddyv2.core.repositories.account.AccountRepository;
import com.traderbuddyv2.core.repositories.security.UserRepository;
import com.traderbuddyv2.core.repositories.trade.TradeRepository;
import com.traderbuddyv2.importing.exceptions.TradeImportFailureException;
import org.assertj.core.groups.Tuple;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ResourceUtils;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.io.FileInputStream;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;


/**
 * Testing class for {@link CMCTradesImportService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class CMCTradesImportServiceTest extends AbstractGenericTest {

    private User user;
    private Account account;

    @Resource(name = "accountRepository")
    private AccountRepository accountRepository;

    @Resource(name = "cmcTradesImportService")
    private CMCTradesImportService cmcTradesImportService;

    @Resource(name = "tradeRepository")
    private TradeRepository tradeRepository;

    @Resource(name = "userRepository")
    private UserRepository userRepository;

    @Before
    public void setUp() {
        account = this.accountRepository.save(generateTestAccount());
        user = generateTestUser();
        user.setAccount(account);
        user = this.userRepository.save(user);
    }

    @After
    public void tearDown() {
        this.accountRepository.delete(account);
        account = null;

        this.userRepository.delete(user);
        user = null;
    }


    //  METHODS

    @Test
    @Order(1)
    @Transactional
    public void test_importTrades_failure() {
        assertThatExceptionOfType(TradeImportFailureException.class)
                .isThrownBy(() -> this.cmcTradesImportService.importTrades("src/main/resources/testing/NotFound.csv", ';'))
                .withMessageContaining("The import process failed with reason");
    }

    @Test
    @Order(2)
    @Transactional
    @WithMockUser(username = "test")
    public void test_importTrades_success() {

        this.cmcTradesImportService.importTrades("classpath:testing/History.csv", ';');

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
    @Transactional
    @WithMockUser(username = "test")
    public void testImportTrades_success_unchanged() {

        this.cmcTradesImportService.importTrades("classpath:testing/History.csv", ';');
        this.cmcTradesImportService.importTrades("classpath:testing/History.csv", ';');

        assertThat(this.tradeRepository.findAll())
                .hasSize(3);
    }

    @Test
    @Order(4)
    @Transactional
    @WithMockUser(username = "test")
    public void test_importTrades_success_inputStream() throws Exception {

        this.cmcTradesImportService.importTrades(new FileInputStream(ResourceUtils.getFile("classpath:testing/History.csv")), ';');

        assertThat(this.tradeRepository.findAll())
                .hasSize(3)
                .extracting("tradeId", "lotSize", "tradeOpenTime", "tradeCloseTime", "openPrice", "closePrice", "netProfit")
                .contains(
                        Tuple.tuple("O5-77-5H7P05", 0.80, LocalDateTime.of(2022, 8, 24, 11, 23), LocalDateTime.of(2022, 8, 24, 11, 27), 12960.00, 12972.38, 12.78),
                        Tuple.tuple("O5-77-5H7MXX", 0.75, LocalDateTime.of(2022, 8, 24, 11, 13), LocalDateTime.of(2022, 8, 24, 11, 14), 12935.17, 12943.36, -8.0),
                        Tuple.tuple("1109841303", 0.0, LocalDateTime.of(2022, 8, 24, 11, 14), LocalDateTime.of(2022, 8, 24, 11, 14), 0.0, 0.0, 8.0)
                );
    }
}
