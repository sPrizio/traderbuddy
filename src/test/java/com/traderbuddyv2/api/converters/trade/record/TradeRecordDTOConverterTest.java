package com.traderbuddyv2.api.converters.trade.record;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.converters.account.AccountDTOConverter;
import com.traderbuddyv2.api.models.dto.trade.record.TradeRecordDTO;
import com.traderbuddyv2.core.services.math.MathService;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link TradeRecordDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TradeRecordDTOConverterTest extends AbstractGenericTest {

    @Mock
    private AccountDTOConverter accountDTOConverter;

    @Mock
    private MathService mathService;

    @Mock
    private TradeRecordStatisticsDTOConverter tradeRecordStatisticsDTOConverter;

    @Mock
    private UniqueIdentifierService uniqueIdentifierService;

    @InjectMocks
    private TradeRecordDTOConverter tradeRecordDTOConverter = new TradeRecordDTOConverter();

    @Before
    public void setUp() {
        Mockito.when(this.accountDTOConverter.convert(any())).thenReturn(generateTestAccountDTO());
        Mockito.when(this.mathService.getDouble(1000.0)).thenReturn(1000.0);
        Mockito.when(this.tradeRecordStatisticsDTOConverter.convert(any())).thenReturn(null);
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
    }


    //  ----------------- convert -----------------

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.tradeRecordDTOConverter.convert(null))
                .isNotNull()
                .satisfies(TradeRecordDTO::isEmpty);
    }

    @Test
    public void test_convert_success() {
        assertThat(this.tradeRecordDTOConverter.convert(generateTestTradeRecord()))
                .isNotNull()
                .extracting("balance", "startDate")
                .containsExactly(1000.0, LocalDate.of(2022, 8, 24));
    }


    //  ----------------- convertAll -----------------

    @Test
    public void test_convertAll_success() {
        assertThat(this.tradeRecordDTOConverter.convertAll(List.of(generateTestTradeRecord())))
                .isNotEmpty()
                .first()
                .extracting("balance", "startDate")
                .containsExactly(1000.0, LocalDate.of(2022, 8, 24));
    }
}
