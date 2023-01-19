package com.traderbuddyv2.core.services.news;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import com.traderbuddyv2.core.repositories.news.MarketNewsRepository;
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
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link MarketNewsService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class MarketNewsServiceTest extends AbstractGenericTest {

    @Autowired
    private MarketNewsService marketNewsService;

    @MockBean
    private MarketNewsRepository marketNewsRepository;

    @Before
    public void setUp() {
        Mockito.when(this.marketNewsRepository.findNewsWithinInterval(any(), any())).thenReturn(List.of(generateMarketNews()));
    }


    //  ----------------- findNewsWithinInterval -----------------

    @Test
    public void test_findNewsWithinInterval_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.marketNewsService.findNewsWithinInterval(null, LocalDate.MAX))
                .withMessage(CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.marketNewsService.findNewsWithinInterval(LocalDate.MIN, null))
                .withMessage(CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);
    }

    @Test
    public void test_findNewsWithinInterval_success() {
        assertThat(this.marketNewsService.findNewsWithinInterval(LocalDate.MIN, LocalDate.MAX))
                .isNotNull()
                .first()
                .extracting("date")
                .isEqualTo(LocalDate.of(2023, 1, 19));
    }
}
