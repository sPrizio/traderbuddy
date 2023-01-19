package com.traderbuddyv2.api.converters.news;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.models.dto.news.MarketNewsSlotDTO;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link MarketNewsSlotDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MarketNewsSlotDTOConverterTest extends AbstractGenericTest {

    @Autowired
    private MarketNewsSlotDTOConverter marketNewsSlotDTOConverter;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @MockBean
    private MarketNewsEntryDTOConverter marketNewsEntryDTOConverter;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.marketNewsEntryDTOConverter.convertAll(any())).thenReturn(List.of());
    }


    //  ----------------- convert -----------------

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.marketNewsSlotDTOConverter.convert(null))
                .isNotNull()
                .satisfies(MarketNewsSlotDTO::isEmpty);

    }

    @Test
    public void test_convert_success() {
        assertThat(this.marketNewsSlotDTOConverter.convert(generateTestMarketNewsSlot()))
                .isNotNull()
                .extracting("time")
                .isEqualTo(LocalTime.of(13, 10));
    }


    //  ----------------- convertAll -----------------

    @Test
    public void test_convertAll_success() {
        assertThat(this.marketNewsSlotDTOConverter.convertAll(List.of(generateTestMarketNewsSlot())))
                .isNotEmpty()
                .first()
                .extracting("time")
                .isEqualTo(LocalTime.of(13, 10));
    }
}
