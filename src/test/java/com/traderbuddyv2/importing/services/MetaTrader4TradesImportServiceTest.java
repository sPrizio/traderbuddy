package com.traderbuddyv2.importing.services;

import com.traderbuddyv2.AbstractGenericTest;
import org.junit.Test;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.annotation.Resource;

/**
 * Testing class for {@link MetaTrader4TradesImportService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class MetaTrader4TradesImportServiceTest extends AbstractGenericTest {

    @Resource(name = "metaTrader4TradesImportService")
    private MetaTrader4TradesImportService metaTrader4TradesImportService;

    @Test
    public void test() {
        this.metaTrader4TradesImportService.importTrades("classpath:testing/Statement.htm", null);
    }
}
