package com.stephenprizio.traderbuddy.validation;

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
        throw new UnsupportedOperationException("Static classes should not be instantiated");
    }


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
     * Validates that the given {@link Collection} only contains 1 element
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
     * Validates that the given {@link Collection} is not empty
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
     * Validates that the 2 given {@link LocalDateTime}s are valid and don't overlap
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

    /**
     * Validates if the given date is of the given format
     *
     * @param date date to test
     * @param format date expected format
     * @param message error message
     * @param values error message values
     */
    public static void validateLocalDateFormat(String date, String format, String message, Object... values) {
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
     * @param message error message
     * @param values error message values
     */
    public static void validateIfPresent(Optional<?> optional, String message, Object... values) {
        if (optional.isEmpty()) {
            throw new NoResultFoundException(String.format(message, values));
        }
    }

    /**
     * Validates if the given json map contains the given keys
     *
     * @param json json as a {@link Map}
     * @param keys required keys
     * @param message error message
     * @param values error message values
     */
    public static void validateJsonIntegrity(Map<String, Object> json, List<String> keys, String message, Object... values) {
        keys.forEach(key -> {
            if (!json.containsKey(key)) {
                throw new JsonMissingPropertyException(String.format(message, values));
            }
        });
    }

    /**
     * Validates that the given {@link MultipartFile}'s extension matches the expected one
     *
     * @param file {@link MultipartFile}
     * @param expectedExtension expected extension (.docx, .txt, etc...)
     * @param message error message
     * @param values error message values
     */
    public static void validateImportFileExtension(MultipartFile file, String expectedExtension, String message, Object... values) {
        if (!FilenameUtils.getExtension(file.getOriginalFilename()).equals(expectedExtension)) {
            throw new FileExtensionNotSupportedException(String.format(message, values));
        }
    }

    /**
     * Validates that the given {@link File}'s extension matches the expected one
     *
     * @param file {@link File}
     * @param expectedExtension expected extension (.docx, .txt, etc...)
     * @param message error message
     * @param values error message values
     */
    public static void validateImportFileExtension(File file, String expectedExtension, String message, Object... values) {
        if (!FilenameUtils.getExtension(file.getName()).equals(expectedExtension)) {
            throw new FileExtensionNotSupportedException(String.format(message, values));
        }
    }
}
