package com.traderbuddyv2.api.converters.news;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.api.models.dto.news.MarketNewsDTO;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link MarketNewsDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MarketNewsDTOConverterTest extends AbstractGenericTest {

    @Autowired
    private MarketNewsDTOConverter marketNewsDTOConverter;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @MockBean
    private MarketNewsSlotDTOConverter marketNewsSlotDTOConverter;

    @Before
    public void setUp() {
        Mockito.when(this.uniqueIdentifierService.generateUid(any())).thenReturn("MTE4");
        Mockito.when(this.marketNewsSlotDTOConverter.convertAll(any())).thenReturn(List.of());
    }


    //  ----------------- convert -----------------

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.marketNewsDTOConverter.convert(null))
                .isNotNull()
                .satisfies(MarketNewsDTO::isEmpty);

    }

    @Test
    public void test_convert_success() {
        assertThat(this.marketNewsDTOConverter.convert(generateMarketNews()))
                .isNotNull()
                .extracting("date")
                .isEqualTo(LocalDate.of(2023, 1, 19));
    }


    //  ----------------- convertAll -----------------

    @Test
    public void test_convertAll_success() {
        assertThat(this.marketNewsDTOConverter.convertAll(List.of(generateMarketNews())))
                .isNotEmpty()
                .first()
                .extracting("date")
                .isEqualTo(LocalDate.of(2023, 1, 19));
    }
}
