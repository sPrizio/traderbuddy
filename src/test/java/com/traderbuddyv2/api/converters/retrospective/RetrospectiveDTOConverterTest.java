package com.traderbuddyv2.api.converters.retrospective;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.models.dto.retrospective.RetrospectiveDTO;
import com.traderbuddyv2.api.models.dto.retrospective.RetrospectiveEntryDTO;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import com.traderbuddyv2.core.services.trade.record.TradeRecordService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link RetrospectiveDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RetrospectiveDTOConverterTest extends AbstractGenericTest {

    @Autowired
    private RetrospectiveDTOConverter retrospectiveDTOConverter;

    @MockBean
    private RetrospectiveEntryDTOConverter retrospectiveEntryDTOConverter;

    @MockBean
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @MockBean
    private TradeRecordService tradeRecordService;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;


    @Before
    public void setUp() {
        Mockito.when(this.retrospectiveEntryDTOConverter.convert(any())).thenReturn(new RetrospectiveEntryDTO());
        Mockito.when(this.retrospectiveEntryDTOConverter.convertAll(any())).thenReturn(List.of());
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.traderBuddyUserDetailsService.getCurrentUser()).thenReturn(generateTestUser());
        Mockito.when(this.tradeRecordService.findTradeRecordForStartDateAndEndDateAndInterval(any(), any(), any())).thenReturn(Optional.empty());
    }


    //  ----------------- convert -----------------

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.retrospectiveDTOConverter.convert(null))
                .isNotNull()
                .satisfies(RetrospectiveDTO::isEmpty);

    }

    @Test
    public void test_convert_success() {
        assertThat(this.retrospectiveDTOConverter.convert(generateRetrospectives().get(0)))
                .isNotNull()
                .extracting("startDate", "endDate")
                .containsExactly(
                        LocalDate.of(2022, 9, 5),
                        LocalDate.of(2022, 9, 11)
                );
    }


    //  ----------------- convertAll -----------------

    @Test
    public void test_convertAll_success() {
        assertThat(this.retrospectiveDTOConverter.convertAll(generateRetrospectives()))
                .isNotEmpty()
                .first()
                .extracting("startDate", "endDate")
                .containsExactly(
                        LocalDate.of(2022, 9, 5),
                        LocalDate.of(2022, 9, 11)
                );
    }
}
