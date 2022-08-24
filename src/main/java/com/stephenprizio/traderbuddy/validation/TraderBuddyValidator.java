package com.stephenprizio.traderbuddy.validation;

import com.stephenprizio.traderbuddy.exceptions.IllegalParameterException;
import com.stephenprizio.traderbuddy.exceptions.NoResultFoundException;
import com.stephenprizio.traderbuddy.exceptions.NonUniqueItemFoundException;
import org.apache.commons.collections4.CollectionUtils;

import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.Objects;

/**
 * Validator class for method integrity
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class TraderBuddyValidator {


    //  METHODS

    /**
     * Validates if the given parameter is not null
     *
     * @param param parameter to test
     * @param message error message
     * @param values error message values
     * @throws IllegalParameterException if parameter is null
     */
    public static void validateParameterIsNotNull(Object param, String message, Object... values) {
        if (Objects.isNull(param)) {
            throw new IllegalParameterException(String.format(message, values));
        }
    }

    /**
     * Validates that the given collection only contains 1 element
     *
     * @param collection collection to test
     * @param message error message
     * @param values error message values
     * @throws NonUniqueItemFoundException if collection size is greater than 1
     */
    public static void validateIfSingleResult(Collection<? extends Object> collection, String message, Object... values) {
        if (collection.size() > 1) {
            throw new NonUniqueItemFoundException(String.format(message, values));
        }
    }

    /**
     * Validates that the given collection is not empty
     *
     * @param collection collection to test
     * @param message error message
     * @param values error message values
     * @throws NoResultFoundException if collection is empty
     */
    public static void validateIfAnyResult(Collection<? extends Object> collection, String message, Object... values) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new NoResultFoundException(String.format(message, values));
        }
    }

    /**
     * Validates that the 2 given dates are valid and don't overlap
     *
     * @param compareOne start date
     * @param compareTwo end date
     * @param message error message
     * @param values error message values
     */
    public static void validateDatesAreNotMutuallyExclusive(LocalDateTime compareOne, LocalDateTime compareTwo, String message, Object... values) {
        if (compareOne.isAfter(compareTwo) || compareTwo.isBefore(compareOne)) {
            throw new UnsupportedOperationException(String.format(message, values));
        }
    }

    /**
     * Validates if the given date & time is of the given format
     *
     * @param date date to test
     * @param format date expected format
     * @param message error message
     * @param values error message values
     */
    public static void validateLocalDateTimeFormat(String date, String format, String message, Object... values) {
        try {
            LocalDateTime.parse(date, DateTimeFormatter.ofPattern(format));
        } catch (Exception e) {
            throw new DateTimeException(String.format(message, values));
        }
    }
}
