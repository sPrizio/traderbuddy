package com.stephenprizio.traderbuddy.converters.retrospectives;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.models.dto.retrospectives.RetrospectiveDTO;
import com.stephenprizio.traderbuddy.models.dto.retrospectives.RetrospectiveEntryDTO;
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


    //  METHODS

    @Before
    public void setUp() {
        Mockito.when(this.retrospectiveEntryDTOConverter.convert(any())).thenReturn(new RetrospectiveEntryDTO());
        Mockito.when(this.retrospectiveEntryDTOConverter.convertAll(any())).thenReturn(List.of());
    }

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
