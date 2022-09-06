package com.stephenprizio.traderbuddy.converters.goals;

import com.stephenprizio.traderbuddy.AbstractGenericTest;
import com.stephenprizio.traderbuddy.enums.calculator.CompoundFrequency;
import com.stephenprizio.traderbuddy.enums.goals.GoalStatus;
import com.stephenprizio.traderbuddy.models.dto.goals.GoalDTO;
import org.junit.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Testing class for {@link GoalDTOConverter}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class GoalDTOConverterTest extends AbstractGenericTest {

    private final GoalDTOConverter goalDTOConverter = new GoalDTOConverter();

    @Test
    public void test_convert_success_emptyResult() {
        assertThat(this.goalDTOConverter.convert(null))
                .isNotNull()
                .satisfies(GoalDTO::isEmpty);

    }

    @Test
    public void test_convert_success() {
        assertThat(this.goalDTOConverter.convert(generateTestGoal()))
                .isNotNull()
                .extracting("active", "name", "startDate", "endDate", "profitTarget", "status", "compoundFrequency", "startingBalance")
                .containsExactly(
                        true,
                        "Test Goal Active",
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2025, 1, 1),
                        528491.0,
                        GoalStatus.IN_PROGRESS,
                        CompoundFrequency.DAILY,
                        1000.0
                );

    }

    @Test
    public void test_convertAll_success() {
        assertThat(this.goalDTOConverter.convertAll(List.of(generateTestGoal())))
                .isNotEmpty()
                .first()
                .extracting("active", "name", "startDate", "endDate", "profitTarget", "status", "compoundFrequency", "startingBalance")
                .containsExactly(
                        true,
                        "Test Goal Active",
                        LocalDate.of(2022, 1, 1),
                        LocalDate.of(2025, 1, 1),
                        528491.0,
                        GoalStatus.IN_PROGRESS,
                        CompoundFrequency.DAILY,
                        1000.0
                );
    }
}
