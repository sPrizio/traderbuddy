package com.stephenprizio.traderbuddy.services.retrospectives;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.exceptions.validation.IllegalParameterException;
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
        Mockito.when(this.retrospectiveRepository.findAllRetrospectivesWithinDate(any(), any())).thenReturn(generateRetrospectives());
    }


    //  ----------------- findAllRetrospectivesWithinDate -----------------

    @Test
    public void test_findAllRetrospectivesWithinDate_missingParams() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.retrospectiveService.findAllRetrospectivesWithinDate(null, LocalDate.MAX))
                .withMessage("startDate cannot be null");

        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> this.retrospectiveService.findAllRetrospectivesWithinDate(LocalDate.MIN, null))
                .withMessage("endDate cannot be null");

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> this.retrospectiveService.findAllRetrospectivesWithinDate(LocalDate.MAX, LocalDate.MIN))
                .withMessage("startDate was after endDate or vice versa");
    }

    @Test
    public void test_findAllRetrospectivesWithinDate_success() {
        assertThat(this.retrospectiveService.findAllRetrospectivesWithinDate(LocalDate.MIN, LocalDate.MAX))
                .hasSize(2)
                .extracting("startDate")
                .containsExactly(LocalDate.of(2022, 9, 5), LocalDate.of(2022, 9, 12));
    }
}
