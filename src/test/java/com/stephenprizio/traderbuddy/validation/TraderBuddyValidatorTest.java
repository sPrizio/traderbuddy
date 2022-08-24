package com.stephenprizio.traderbuddy.validation;

import com.stephenprizio.traderbuddy.exceptions.IllegalParameterException;
import com.stephenprizio.traderbuddy.exceptions.NoResultFoundException;
import com.stephenprizio.traderbuddy.exceptions.NonUniqueItemFoundException;
import org.junit.jupiter.api.Test;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link TraderBuddyValidator}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TraderBuddyValidatorTest {

    @Test
    public void test_validateParameterIsNotNull_success() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> TraderBuddyValidator.validateParameterIsNotNull(null, "This is a null test"))
                .withMessage("This is a null test");
    }

    @Test
    public void test_validateIfSingleResult_success() {
        assertThatExceptionOfType(NonUniqueItemFoundException.class)
                .isThrownBy(() -> TraderBuddyValidator.validateIfSingleResult(List.of("one", "two"), "This is a single test"))
                .withMessage("This is a single test");
    }

    @Test
    public void test_validateIfAnyResult_success() {
        assertThatExceptionOfType(NoResultFoundException.class)
                .isThrownBy(() -> TraderBuddyValidator.validateIfAnyResult(List.of(), "This is an empty collection test"))
                .withMessage("This is an empty collection test");
    }

    @Test
    public void test_validateDatesAreNotMutuallyExclusive_success() {
        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> TraderBuddyValidator.validateDatesAreNotMutuallyExclusive(LocalDate.of(2022, 1, 1).atStartOfDay(), LocalDate.of(2021, 1, 1).atStartOfDay(), "This is a two bad dates test"))
                .withMessage("This is a two bad dates test");
    }

    @Test
    public void test_validateLocalDateTimeFormat_success() {
        assertThatExceptionOfType(DateTimeException.class)
                .isThrownBy(() -> TraderBuddyValidator.validateLocalDateTimeFormat("1/1/1999", "yyyy-MM-dd'T'HH:mm:ss", "This is a bad date test"))
                .withMessage("This is a bad date test");
    }
}
