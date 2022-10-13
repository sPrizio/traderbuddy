package com.stephenprizio.traderbuddy.validation;

import com.stephenprizio.traderbuddy.constants.TraderBuddyConstants;
import com.stephenprizio.traderbuddy.exceptions.calculator.UnexpectedNegativeValueException;
import com.stephenprizio.traderbuddy.exceptions.calculator.UnexpectedZeroValueException;
import com.stephenprizio.traderbuddy.exceptions.importing.FileExtensionNotSupportedException;
import com.stephenprizio.traderbuddy.exceptions.validation.IllegalParameterException;
import com.stephenprizio.traderbuddy.exceptions.validation.JsonMissingPropertyException;
import com.stephenprizio.traderbuddy.exceptions.validation.NoResultFoundException;
import com.stephenprizio.traderbuddy.exceptions.validation.NonUniqueItemFoundException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * Validator class for method integrity
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class GenericValidator {

    private GenericValidator() {
        throw new UnsupportedOperationException(String.format(TraderBuddyConstants.NO_INSTANTIATION, getClass().getName()));
    }


    //  METHODS

    /**
     * Validates if the given parameter is not null
     *
     * @param param   parameter to test
     * @param message error message
     * @param values  error message values
     * @throws IllegalParameterException if parameter is null
     */
    public static void validateParameterIsNotNull(final Object param, final String message, final Object... values) {
        if (Objects.isNull(param)) {
            throw new IllegalParameterException(String.format(message, values));
        }
    }

    /**
     * Validates that the given {@link Collection} only contains 1 element
     *
     * @param collection collection to test
     * @param message    error message
     * @param values     error message values
     * @throws NonUniqueItemFoundException if collection size is greater than 1
     */
    public static void validateIfSingleResult(final Collection<? extends Object> collection, final String message, final Object... values) {
        if (collection.size() > 1) {
            throw new NonUniqueItemFoundException(String.format(message, values));
        }
    }

    /**
     * Validates that the given {@link Collection} is not empty
     *
     * @param collection collection to test
     * @param message    error message
     * @param values     error message values
     * @throws NoResultFoundException if collection is empty
     */
    public static void validateIfAnyResult(final Collection<? extends Object> collection, final String message, final Object... values) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new NoResultFoundException(String.format(message, values));
        }
    }

    /**
     * Validates that the 2 given {@link LocalDateTime}s are valid and don't overlap
     *
     * @param compareOne start date
     * @param compareTwo end date
     * @param message    error message
     * @param values     error message values
     * @throws UnsupportedOperationException if dates are exclusive
     */
    public static void validateDatesAreNotMutuallyExclusive(final LocalDateTime compareOne, final LocalDateTime compareTwo, final String message, final Object... values) {
        if (compareOne.isAfter(compareTwo) || compareTwo.isBefore(compareOne)) {
            throw new UnsupportedOperationException(String.format(message, values));
        }
    }

    /**
     * Validates if the given date & time is of the given format
     *
     * @param date    date to test
     * @param format  date expected format
     * @param message error message
     * @param values  error message values
     * @throws DateTimeException for bad format
     */
    public static void validateLocalDateTimeFormat(final String date, final String format, final String message, final Object... values) {
        try {
            LocalDateTime.parse(date, DateTimeFormatter.ofPattern(format));
        } catch (Exception e) {
            throw new DateTimeException(String.format(message, values));
        }
    }

    /**
     * Validates if the given date is of the given format
     *
     * @param date    date to test
     * @param format  date expected format
     * @param message error message
     * @param values  error message values
     * @throws DateTimeException for bad format
     */
    public static void validateLocalDateFormat(final String date, final String format, final String message, final Object... values) {
        try {
            LocalDate.parse(date, DateTimeFormatter.ofPattern(format));
        } catch (Exception e) {
            throw new DateTimeException(String.format(message, values));
        }
    }

    /**
     * Validates if the given {@link Optional} is empty
     *
     * @param optional {@link Optional}
     * @param message  error message
     * @param values   error message values
     * @throws NoResultFoundException if optional is empty
     */
    public static void validateIfPresent(final Optional<?> optional, final String message, final Object... values) {
        if (optional.isEmpty()) {
            throw new NoResultFoundException(String.format(message, values));
        }
    }

    /**
     * Validates if the given json map contains the given keys
     *
     * @param json    json as a {@link Map}
     * @param keys    required keys
     * @param message error message
     * @param values  error message values
     * @throws JsonMissingPropertyException is json is missing a required property
     */
    public static void validateJsonIntegrity(final Map<String, Object> json, final List<String> keys, final String message, final Object... values) {
        keys.forEach(key -> {
            if (!json.containsKey(key)) {
                throw new JsonMissingPropertyException(String.format(message, values));
            }
        });
    }

    /**
     * Validates that the given {@link MultipartFile}'s extension matches the expected one
     *
     * @param file              {@link MultipartFile}
     * @param expectedExtension expected extension (.docx, .txt, etc...)
     * @param message           error message
     * @param values            error message values
     * @throws FileExtensionNotSupportedException if file extension is not supported
     */
    public static void validateImportFileExtension(final MultipartFile file, final String expectedExtension, final String message, final Object... values) {
        if (!FilenameUtils.getExtension(file.getOriginalFilename()).equals(expectedExtension)) {
            throw new FileExtensionNotSupportedException(String.format(message, values));
        }
    }

    /**
     * Validates that the given {@link File}'s extension matches the expected one
     *
     * @param file              {@link File}
     * @param expectedExtension expected extension (.docx, .txt, etc...)
     * @param message           error message
     * @param values            error message values
     * @throws FileExtensionNotSupportedException is file extension is not supported
     */
    public static void validateImportFileExtension(final File file, final String expectedExtension, final String message, final Object... values) {
        if (!FilenameUtils.getExtension(file.getName()).equals(expectedExtension)) {
            throw new FileExtensionNotSupportedException(String.format(message, values));
        }
    }

    /**
     * Validates that the given {@link Number} is not negative
     *
     * @param number  {@link Number}
     * @param message error message
     * @param values  error message values
     * @throws UnexpectedNegativeValueException if value is negative
     */
    public static void validateNonNegativeValue(final Number number, final String message, final Object... values) {

        validateSupportedNumericalType(number);

        if (number instanceof Integer num && num < 0) {
            throw new UnexpectedNegativeValueException(String.format(message, values));
        }

        if (number instanceof Long num && num < 0) {
            throw new UnexpectedNegativeValueException(String.format(message, values));
        }

        if (number instanceof Float num && num < 0.0) {
            throw new UnexpectedNegativeValueException(String.format(message, values));
        }

        if (number instanceof Double num && num < 0.0) {
            throw new UnexpectedNegativeValueException(String.format(message, values));
        }
    }

    /**
     * Validates that the given {@link Number} is not zero
     *
     * @param number  {@link Number}
     * @param message error message
     * @param values  error message values
     * @throws UnexpectedZeroValueException if value is zero
     */
    public static void validateNonZeroValue(final Number number, final String message, final Object... values) {

        validateSupportedNumericalType(number);

        if (number instanceof Integer num && num == 0) {
            throw new UnexpectedZeroValueException(String.format(message, values));
        }

        if (number instanceof Long num && num == 0) {
            throw new UnexpectedZeroValueException(String.format(message, values));
        }

        if (number instanceof Float num && num == 0.0) {
            throw new UnexpectedZeroValueException(String.format(message, values));
        }

        if (number instanceof Double num && num == 0.0) {
            throw new UnexpectedZeroValueException(String.format(message, values));
        }
    }

    /**
     * Validates that the given {@link Integer} is not greater than the maximum allowable within the app
     *
     * @param year year value
     * @param message error message
     * @param values error message values
     */
    public static void validateAcceptableYear(final Integer year, final String message, final Object... values) {

        validateSupportedNumericalType(year);
        validateNonNegativeValue(year, message, values);
        validateNonZeroValue(year, message, values);

        if (year > TraderBuddyConstants.MAX_CALENDAR_YEAR) {
            throw new IllegalArgumentException(String.format("The given year %s was higher than the maximum allowable %d", year, TraderBuddyConstants.MAX_CALENDAR_YEAR));
        }
    }


    //  HELPERS

    /**
     * Validates the given {@link Number} is supported
     *
     * @param number {@link Number}
     * @throws UnsupportedOperationException if value is not one of these 4: {@link Integer}, {@link Long}, {@link Float}, {@link Double}
     */
    private static void validateSupportedNumericalType(final Number number) {
        if (!(number instanceof Integer || number instanceof Long || number instanceof Float || number instanceof Double)) {
            throw new UnsupportedOperationException(String.format("number %s was not of the supported type [Integer, Long, Float, Double]", number.getClass()));
        }
    }
}
