package com.stephenprizio.traderbuddy.validation;

import com.stephenprizio.traderbuddy.exceptions.importing.FileExtensionNotSupportedException;
import com.stephenprizio.traderbuddy.exceptions.validation.IllegalParameterException;
import com.stephenprizio.traderbuddy.exceptions.validation.JsonMissingPropertyException;
import com.stephenprizio.traderbuddy.exceptions.validation.NoResultFoundException;
import com.stephenprizio.traderbuddy.exceptions.validation.NonUniqueItemFoundException;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link GenericValidator}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class GenericValidatorTest {

    @Test
    public void test_validateParameterIsNotNull_success() {
        assertThatExceptionOfType(IllegalParameterException.class)
                .isThrownBy(() -> GenericValidator.validateParameterIsNotNull(null, "This is a null test"))
                .withMessage("This is a null test");
    }

    @Test
    public void test_validateIfSingleResult_success() {
        List<String> list = List.of("one", "two");
        assertThatExceptionOfType(NonUniqueItemFoundException.class)
                .isThrownBy(() -> GenericValidator.validateIfSingleResult(list, "This is a single test"))
                .withMessage("This is a single test");
    }

    @Test
    public void test_validateIfAnyResult_success() {
        List<Object> list = List.of();
        assertThatExceptionOfType(NoResultFoundException.class)
                .isThrownBy(() -> GenericValidator.validateIfAnyResult(list, "This is an empty collection test"))
                .withMessage("This is an empty collection test");
    }

    @Test
    public void test_validateDatesAreNotMutuallyExclusive_success() {
        LocalDateTime test1 = LocalDate.of(2022, 1, 1).atStartOfDay();
        LocalDateTime test2 = LocalDate.of(2021, 1, 1).atStartOfDay();

        assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(() -> GenericValidator.validateDatesAreNotMutuallyExclusive(test1, test2, "This is a two bad dates test"))
                .withMessage("This is a two bad dates test");
    }

    @Test
    public void test_validateLocalDateTimeFormat_success() {
        assertThatExceptionOfType(DateTimeException.class)
                .isThrownBy(() -> GenericValidator.validateLocalDateTimeFormat("1/1/1999", "yyyy-MM-dd'T'HH:mm:ss", "This is a bad date test"))
                .withMessage("This is a bad date test");
    }

    @Test
    public void test_validateIfPresent_success() {
        Optional<Integer> optional = Optional.empty();
        assertThatExceptionOfType(NoResultFoundException.class)
                .isThrownBy(() -> GenericValidator.validateIfPresent(optional, "This is an empty optional test"))
                .withMessage("This is an empty optional test");
    }

    @Test
    public void test_validateJsonIntegrity_success() {
        Map<String, Object> map = Map.of("test", "value");
        List<String> list = List.of("missing");

        assertThatExceptionOfType(JsonMissingPropertyException.class)
                .isThrownBy(() -> GenericValidator.validateJsonIntegrity(map, list, "This is an empty json test"))
                .withMessage("This is an empty json test");
    }

    @Test
    public void test_validateImportFileExtension_multipart_success() {
        MockMultipartFile testFile = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        assertThatExceptionOfType(FileExtensionNotSupportedException.class)
                .isThrownBy(() -> GenericValidator.validateImportFileExtension(testFile, "csv", "This is an empty multipart file test"))
                .withMessage("This is an empty multipart file test");
    }

    @Test
    public void test_validateImportFileExtension_success() {
        File file = new File("test.txt");
        assertThatExceptionOfType(FileExtensionNotSupportedException.class)
                .isThrownBy(() -> GenericValidator.validateImportFileExtension(file, "csv", "This is an empty file test"))
                .withMessage("This is an empty file test");
        file.delete();
    }
}
