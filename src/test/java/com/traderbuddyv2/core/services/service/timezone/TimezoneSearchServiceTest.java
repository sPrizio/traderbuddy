package com.traderbuddyv2.core.services.service.timezone;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import com.traderbuddyv2.core.services.search.timezone.TimezoneSearchService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link TimezoneSearchService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class TimezoneSearchServiceTest extends AbstractGenericTest {

    @Autowired
    private TimezoneSearchService timezoneSearchService;


    //  ----------------- search -----------------

    @Test
    public void test_search_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.timezoneSearchService.search(null))
                .withMessage(CoreConstants.Validation.SEARCH_QUERY_CANNOT_BE_NULL);
    }

    @Test
    public void test_search_success() {
        assertThat(this.timezoneSearchService.search("america"))
                .isNotEmpty();
    }

    @Test
    public void test_search_limited_success() {
        assertThat(this.timezoneSearchService.search("america", 10))
                .hasSize(10);
    }
}
