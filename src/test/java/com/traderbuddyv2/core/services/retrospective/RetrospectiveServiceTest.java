package com.traderbuddyv2.core.services.retrospective;

import com.traderbuddyv2.AbstractGenericTest;
import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.core.enums.interval.AggregateInterval;
import com.traderbuddyv2.core.exceptions.system.EntityCreationException;
import com.traderbuddyv2.core.exceptions.system.EntityModificationException;
import com.traderbuddyv2.core.exceptions.validation.IllegalParameterException;
import com.traderbuddyv2.core.exceptions.validation.MissingRequiredDataException;
import com.traderbuddyv2.core.repositories.retrospective.AudioRetrospectiveRepository;
import com.traderbuddyv2.core.repositories.retrospective.RetrospectiveRepository;
import com.traderbuddyv2.core.services.platform.UniqueIdentifierService;
import com.traderbuddyv2.core.services.security.TraderBuddyUserDetailsService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;

/**
 * Testing class for {@link RetrospectiveService}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class RetrospectiveServiceTest extends AbstractGenericTest {

    @MockBean
    private AudioRetrospectiveRepository audioRetrospectiveRepository;

    @MockBean
    private RetrospectiveRepository retrospectiveRepository;

    @Autowired
    private RetrospectiveService retrospectiveService;

    @MockBean
    private TraderBuddyUserDetailsService traderBuddyUserDetailsService;

    @MockBean
    private UniqueIdentifierService uniqueIdentifierService;

    @Before
    public void setUp() {
        Mockito.when(this.retrospectiveRepository.findAllRetrospectivesWithinDate(any(), any(), any(), any())).thenReturn(List.of(generateRetrospectives().get(0)));
        Mockito.when(this.retrospectiveRepository.findAllRetrospectivesWithinDate(any(), any(), any())).thenReturn(List.of(generateRetrospectives().get(0)));
        Mockito.when(this.retrospectiveRepository.findRetrospectiveByStartDateAndEndDateAndIntervalFrequencyAndAccount(any(), any(), any(), any())).thenReturn(generateRetrospectives().get(0));
        Mockito.when(this.retrospectiveRepository.save(any())).thenReturn(generateRetrospectives().get(0));
        Mockito.when(this.retrospectiveRepository.findById(1L)).thenReturn(Optional.of(generateRetrospectives().get(0)));
        Mockito.when(this.retrospectiveRepository.findAllByAccount(any())).thenReturn(generateRetrospectives());
        Mockito.when(this.retrospectiveRepository.findTopByIntervalFrequencyAndAccountOrderByStartDateDesc(any(), any())).thenReturn(generateRetrospectives().get(0));
        Mockito.when(this.uniqueIdentifierService.retrieveIdForUid("test")).thenReturn(1L);
        Mockito.when(this.traderBuddyUserDetailsService.getCurrentUser()).thenReturn(generateTestUser());
        Mockito.when(this.audioRetrospectiveRepository.save(any())).thenReturn(null);
    }


    //  ----------------- findAllRetrospectivesWithinDate -----------------

    @Test
    public void test_findAllRetrospectivesWithinDate_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.retrospectiveService.findAllRetrospectivesWithinDate(null, LocalDate.MAX, AggregateInterval.MONTHLY))
                .withMessage(CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.retrospectiveService.findAllRetrospectivesWithinDate(LocalDate.MIN, null, AggregateInterval.MONTHLY))
                .withMessage(CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.retrospectiveService.findAllRetrospectivesWithinDate(LocalDate.MIN, LocalDate.MAX, null))
                .withMessage(CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.retrospectiveService.findAllRetrospectivesWithinDate(LocalDate.MAX, LocalDate.MIN, AggregateInterval.MONTHLY))
                .withMessage(CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    public void test_findAllRetrospectivesWithinDate_success() {
        assertThat(this.retrospectiveService.findAllRetrospectivesWithinDate(LocalDate.MIN, LocalDate.MAX, AggregateInterval.MONTHLY))
                .hasSize(1)
                .extracting("startDate")
                .containsExactly(LocalDate.of(2022, 9, 5));
    }


    //  ----------------- findRetrospectiveForStartDateAndEndDateAndInterval -----------------

    @Test
    public void test_findRetrospectiveForStartDateAndEndDateAndInterval_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.retrospectiveService.findRetrospectiveForStartDateAndEndDateAndInterval(null, LocalDate.MAX, AggregateInterval.MONTHLY))
                .withMessage(CoreConstants.Validation.START_DATE_CANNOT_BE_NULL);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.retrospectiveService.findRetrospectiveForStartDateAndEndDateAndInterval(LocalDate.MIN, null, AggregateInterval.MONTHLY))
                .withMessage(CoreConstants.Validation.END_DATE_CANNOT_BE_NULL);

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.retrospectiveService.findRetrospectiveForStartDateAndEndDateAndInterval(LocalDate.MIN, LocalDate.MAX, null))
                .withMessage(CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.retrospectiveService.findRetrospectiveForStartDateAndEndDateAndInterval(LocalDate.MAX, LocalDate.MIN, AggregateInterval.MONTHLY))
                .withMessage(CoreConstants.Validation.MUTUALLY_EXCLUSIVE_DATES);
    }

    @Test
    public void test_findRetrospectiveForStartDateAndEndDateAndInterval_success() {
        assertThat(this.retrospectiveService.findRetrospectiveForStartDateAndEndDateAndInterval(LocalDate.of(2022, 9, 23), LocalDate.of(2022, 9, 24), AggregateInterval.MONTHLY))
                .isNotEmpty();
    }


    //  ----------------- findRetrospectiveForUid -----------------

    @Test
    public void test_findRetrospectiveForUid_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.retrospectiveService.findRetrospectiveForUid(null))
                .withMessage(CoreConstants.Validation.UID_CANNOT_BE_NULL);
    }

    @Test
    public void test_findRetrospectiveForUid_success() {
        assertThat(this.retrospectiveService.findRetrospectiveForUid("test"))
                .isNotEmpty();
    }


    //  ----------------- findActiveRetrospectiveMonths -----------------

    @Test
    public void test_findActiveRetrospectiveMonths_success() {
        assertThat(this.retrospectiveService.findActiveRetrospectiveMonths(2022))
                .hasSize(1)
                .first()
                .isEqualTo(LocalDate.of(2022, 9, 1));
    }


    //  ----------------- findActiveRetrospectiveYears -----------------

    @Test
    public void test_findActiveRetrospectiveYears_success() {
        assertThat(this.retrospectiveService.findActiveRetrospectiveYears())
                .hasSize(1)
                .first()
                .isEqualTo(LocalDate.of(2022, 1, 1));
    }


    //  ----------------- findMostRecentRetrospectiveForInterval -----------------

    @Test
    public void test_findMostRecentRetrospectiveForInterval_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.retrospectiveService.findMostRecentRetrospectiveForInterval(null))
                .withMessage(CoreConstants.Validation.INTERVAL_CANNOT_BE_NULL);
    }

    @Test
    public void test_findMostRecentRetrospectiveForInterval_success() {
        assertThat(this.retrospectiveService.findMostRecentRetrospectiveForInterval(AggregateInterval.MONTHLY))
                .isNotEmpty();
    }


    //  ----------------- createRetrospective -----------------

    @Test
    public void test_createRetrospective_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.retrospectiveService.createRetrospective(null))
                .withMessage("The required data for creating a Retrospective was null or empty");
    }

    @Test
    public void test_createRetrospective_erroneousCreation() {
        Map<String, Object> map = Map.of("bad", "input");
        assertThatExceptionOfType(EntityCreationException.class)
                .isThrownBy(() -> this.retrospectiveService.createRetrospective(map))
                .withMessage("A Retrospective could not be created : Cannot invoke \"java.util.Map.get(Object)\" because \"retro\" is null");
    }

    @Test
    public void test_createRetrospective_success() {

        Map<String, Object> data =
                Map.of(
                        "retrospective",
                        Map.of(
                                "startDate", "2022-09-05",
                                "endDate", "2022-09-11",
                                "intervalFrequency", "MONTHLY",
                                "points", List.of(
                                        Map.of(
                                                "lineNumber", 1,
                                                "entryText", "Test 1",
                                                "keyPoint", "true"
                                        ),
                                        Map.of(
                                                "lineNumber", 2,
                                                "entryText", "Test 2",
                                                "keyPoint", "false"
                                        )
                                )
                        )
                );

        assertThat(this.retrospectiveService.createRetrospective(data))
                .isNotNull()
                .extracting("startDate", "endDate", "intervalFrequency")
                .containsExactly(LocalDate.of(2022, 9, 5), LocalDate.of(2022, 9, 11), AggregateInterval.MONTHLY);
    }


    //  ----------------- updateRetrospective -----------------

    @Test
    public void test_updateRetrospective_missingData() {
        assertThatExceptionOfType(MissingRequiredDataException.class)
                .isThrownBy(() -> this.retrospectiveService.updateRetrospective("test", null))
                .withMessage("The required data for updating a Retrospective was null or empty");
    }

    @Test
    public void test_updateRetrospective_erroneousModification() {
        Map<String, Object> map = Map.of("bad", "input");
        assertThatExceptionOfType(EntityModificationException.class)
                .isThrownBy(() -> this.retrospectiveService.updateRetrospective("test", map))
                .withMessage("An error occurred while modifying the Retrospective : Cannot invoke \"java.util.Map.get(Object)\" because \"retro\" is null");
    }

    @Test
    public void test_updateRetrospective_success() {

        Map<String, Object> data =
                Map.of(
                        "retrospective",
                        Map.of(
                                "startDate", "2022-09-05",
                                "endDate", "2022-09-11",
                                "intervalFrequency", "MONTHLY",
                                "points", List.of(
                                        Map.of(
                                                "lineNumber", 1,
                                                "entryText", "Test 1",
                                                "keyPoint", "true"
                                        ),
                                        Map.of(
                                                "lineNumber", 2,
                                                "entryText", "Test 2",
                                                "keyPoint", "false"
                                        )
                                )
                        )
                );

        assertThat(this.retrospectiveService.updateRetrospective("test", data))
                .isNotNull()
                .extracting("startDate", "endDate", "intervalFrequency")
                .containsExactly(LocalDate.of(2022, 9, 5), LocalDate.of(2022, 9, 11), AggregateInterval.MONTHLY);
    }


    //  ----------------- deleteRetrospective -----------------

    @Test
    public void test_deleteRetrospective_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.retrospectiveService.deleteRetrospective(null))
                .withMessage(CoreConstants.Validation.UID_CANNOT_BE_NULL);
    }

    @Test
    public void test_deleteRetrospective_unknown_success() {
        assertThat(this.retrospectiveService.deleteRetrospective("unknown"))
                .isFalse();
    }

    @Test
    public void test_deleteRetrospective_success() {
        assertThat(this.retrospectiveService.deleteRetrospective("test"))
                .isTrue();
    }


    //  ----------------- saveAudio -----------------

    @Test
    public void test_saveAudio_success() {

        MultipartFile mockFile = new MockMultipartFile("file", "hello.csv", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());
        assertThat(this.retrospectiveService.saveAudio(LocalDate.MIN, LocalDate.MAX, AggregateInterval.MONTHLY, "Test", mockFile))
                .isEmpty();
    }
}
