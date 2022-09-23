package com.stephenprizio.traderbuddy.services.retrospectives;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.AggregateInterval;
import com.stephenprizio.traderbuddy.exceptions.system.EntityCreationException;
import com.stephenprizio.traderbuddy.exceptions.system.EntityModificationException;
import com.stephenprizio.traderbuddy.exceptions.validation.IllegalParameterException;
import com.stephenprizio.traderbuddy.exceptions.validation.MissingRequiredDataException;
import com.stephenprizio.traderbuddy.repositories.retrospectives.RetrospectiveRepository;
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
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

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
    private RetrospectiveRepository retrospectiveRepository;

    @Autowired
    private RetrospectiveService retrospectiveService;

    @Before
    public void setUp() {
        Mockito.when(this.retrospectiveRepository.findAllRetrospectivesWithinDate(any(), any(), any())).thenReturn(List.of(generateRetrospectives().get(0)));
        Mockito.when(this.retrospectiveRepository.findRetrospectiveByStartDateAndEndDateAndIntervalFrequency(any(), any(), any())).thenReturn(generateRetrospectives().get(0));
        Mockito.when(this.retrospectiveRepository.save(any())).thenReturn(generateRetrospectives().get(0));
    }


    //  ----------------- findAllRetrospectivesWithinDate -----------------

    @Test
    public void test_findAllRetrospectivesWithinDate_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.retrospectiveService.findAllRetrospectivesWithinDate(null, LocalDate.MAX, AggregateInterval.MONTHLY))
                .withMessage("startDate cannot be null");

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.retrospectiveService.findAllRetrospectivesWithinDate(LocalDate.MIN, null, AggregateInterval.MONTHLY))
                .withMessage("endDate cannot be null");

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.retrospectiveService.findAllRetrospectivesWithinDate(LocalDate.MIN, LocalDate.MAX, null))
                .withMessage("interval cannot be null");

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.retrospectiveService.findAllRetrospectivesWithinDate(LocalDate.MAX, LocalDate.MIN, AggregateInterval.MONTHLY))
                .withMessage("startDate was after endDate or vice versa");
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
                .withMessage("start date cannot be null");

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.retrospectiveService.findRetrospectiveForStartDateAndEndDateAndInterval(LocalDate.MIN, null, AggregateInterval.MONTHLY))
                .withMessage("end date cannot be null");

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.retrospectiveService.findRetrospectiveForStartDateAndEndDateAndInterval(LocalDate.MIN, LocalDate.MAX, null))
                .withMessage("interval cannot be null");

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.retrospectiveService.findRetrospectiveForStartDateAndEndDateAndInterval(LocalDate.MAX, LocalDate.MIN, AggregateInterval.MONTHLY))
                .withMessage("startDate was after endDate or vice versa");
    }

    @Test
    public void test_findRetrospectiveForStartDateAndEndDateAndInterval_success() {
        assertThat(this.retrospectiveService.findRetrospectiveForStartDateAndEndDateAndInterval(LocalDate.MIN, LocalDate.MAX, AggregateInterval.MONTHLY))
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
                .isThrownBy(() -> this.retrospectiveService.updateRetrospective(LocalDate.MIN, LocalDate.MAX, AggregateInterval.MONTHLY, null))
                .withMessage("The required data for updating a Retrospective was null or empty");
    }

    @Test
    public void test_updateRetrospective_erroneousModification() {
        Map<String, Object> map = Map.of("bad", "input");
        assertThatExceptionOfType(EntityModificationException.class)
                .isThrownBy(() -> this.retrospectiveService.updateRetrospective(LocalDate.MIN, LocalDate.MAX, AggregateInterval.MONTHLY, map))
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

        assertThat(this.retrospectiveService.updateRetrospective(LocalDate.of(2022, 9, 5), LocalDate.of(2022, 9, 11), AggregateInterval.MONTHLY, data))
                .isNotNull()
                .extracting("startDate", "endDate", "intervalFrequency")
                .containsExactly(LocalDate.of(2022, 9, 5), LocalDate.of(2022, 9, 11), AggregateInterval.MONTHLY);
    }
}
