package com.traderbuddyv2.core.services.news;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.exceptions.system.EntityCreationException;
import com.traderbuddyv2.core.exceptions.system.EntityModificationException;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import com.traderbuddyv2.core.exceptions.validation.MissingRequiredDataException;
import com.traderbuddyv2.core.repositories.news.MarketNewsRepository;
import com.traderbuddyv2.core.repositories.news.MarketNewsSlotRepository;
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
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

    @MockBean
    private MarketNewsSlotRepository marketNewsSlotRepository;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Before
    public void setUp() {
        Mockito.when(this.marketNewsRepository.findNewsWithinInterval(any(), any())).thenReturn(List.of(generateMarketNews()));
        Mockito.when(this.marketNewsRepository.findById(1L)).thenReturn(Optional.of(generateMarketNews()));
        Mockito.when(this.marketNewsRepository.save(any())).thenReturn(generateMarketNews());
        Mockito.when(this.marketNewsSlotRepository.save(any())).thenReturn(generateTestMarketNewsSlot());
        Mockito.when(this.uniqueIdentifierService.retrieveIdForUid("test")).thenReturn(1L);
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


    //  ----------------- createMarketNews -----------------

    @Test
    public void test_createMarketNews_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.marketNewsService.createMarketNews(null))
                .withMessage("The required data for creating a MarketNews entity was null or empty");
    }

    @Test
    public void test_createMarketNews_erroneousCreation() {
        Map<String, Object> map = Map.of("bad", "input");
        assertThatExceptionOfType(EntityCreationException.class)
                .isThrownBy(() -> this.marketNewsService.createMarketNews(map))
                .withMessage("A MarketNews could not be created : Cannot invoke \"java.util.Map.get(Object)\" because \"news\" is null");
    }

    @Test
    public void test_createMarketNews_success() {

        Map<String, Object> data =
                Map.of(
                        "marketNews",
                        Map.of(
                                "date", "2022-09-05",
                                "slots", List.of(
                                        Map.of(
                                                "time", LocalTime.of(14, 0, 0),
                                                "entries", List.of(
                                                        Map.of(
                                                                "content", "Test News Entry 1",
                                                                "severity", 3
                                                        ),
                                                        Map.of(
                                                                "content", "Test News Entry 2",
                                                                "severity", 2
                                                        )
                                                )
                                        ),
                                        Map.of(
                                                "time", LocalTime.of(8, 30, 0),
                                                "entries", List.of(
                                                        Map.of(
                                                                "content", "Test News Entry 3",
                                                                "severity", 1
                                                        )
                                                )
                                        )
                                )
                        )
                );

        assertThat(this.marketNewsService.createMarketNews(data))
                .isNotNull()
                .extracting("date")
                .isEqualTo(LocalDate.of(2023, 1, 19));
    }


    //  ----------------- updateMarketNews -----------------

    @Test
    public void test_updateMarketNews_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.marketNewsService.updateMarketNews("test", null))
                .withMessage("The required data for updating a MarketNews entity was null or empty");
    }

    @Test
    public void test_updateMarketNews_erroneousUpdate() {
        Map<String, Object> map = Map.of("bad", "input");
        assertThatExceptionOfType(EntityModificationException.class)
                .isThrownBy(() -> this.marketNewsService.updateMarketNews("test", map))
                .withMessage("An error occurred while modifying the MarketNews : Cannot invoke \"java.util.Map.get(Object)\" because \"news\" is null");
    }

    @Test
    public void test_updateMarketNews_success() {

        Map<String, Object> data =
                Map.of(
                        "marketNews",
                        Map.of(
                                "date", "2022-09-05",
                                "slots", List.of(
                                        Map.of(
                                                "time", LocalTime.of(14, 0, 0),
                                                "entries", List.of(
                                                        Map.of(
                                                                "content", "Test News Entry 1",
                                                                "severity", 3
                                                        ),
                                                        Map.of(
                                                                "content", "Test News Entry 2",
                                                                "severity", 2
                                                        )
                                                )
                                        ),
                                        Map.of(
                                                "time", LocalTime.of(8, 30, 0),
                                                "entries", List.of(
                                                        Map.of(
                                                                "content", "Test News Entry 3",
                                                                "severity", 1
                                                        )
                                                )
                                        )
                                )
                        )
                );

        assertThat(this.marketNewsService.updateMarketNews("test", data))
                .isNotNull()
                .extracting("date")
                .isEqualTo(LocalDate.of(2023, 1, 19));
    }


    //  ----------------- deleteRetrospective -----------------

    @Test
    public void test_deleteMarketNews_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.marketNewsService.deleteMarketNews(null))
                .withMessage(CoreConstants.Validation.UID_CANNOT_BE_NULL);
    }

    @Test
    public void test_deleteMarketNews_unknown_success() {
        assertThat(this.marketNewsService.deleteMarketNews("unknown"))
                .isFalse();
    }

    @Test
    public void test_deleteMarketNews_success() {
        assertThat(this.marketNewsService.deleteMarketNews("test"))
                .isTrue();
    }
}
