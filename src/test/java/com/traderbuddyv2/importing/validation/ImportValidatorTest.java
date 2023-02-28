package com.traderbuddyv2.importing.validation;

import com.traderbuddyv2.importing.exceptions.FileExtensionNotSupportedException;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * Testing class for {@link ImportValidator}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class ImportValidatorTest {


    //  ----------------- validateImportFileExtension -----------------

    @Test
    public void test_validateImportFileExtension_multipart_success() {
        MockMultipartFile testFile = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        assertThatExceptionOfType(FileExtensionNotSupportedException.class)
                .isThrownBy(() -> ImportValidator.validateImportFileExtension(testFile, "csv", "This is an empty multipart file test"))
                .withMessage("This is an empty multipart file test");
    }

    @Test
    public void test_validateImportFileExtension_success() {
        File file = new File("test.txt");
        assertThatExceptionOfType(FileExtensionNotSupportedException.class)
                .isThrownBy(() -> ImportValidator.validateImportFileExtension(file, "csv", "This is an empty file test"))
                .withMessage("This is an empty file test");
        file.delete();
    }


    //  ----------------- validateAudioImportFileExtensions -----------------

    @Test
    public void test_validateAudioImportFileExtensions_success() {
        MockMultipartFile testFile = new MockMultipartFile("file", "hello.txt", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes());

        assertThatExceptionOfType(FileExtensionNotSupportedException.class)
                .isThrownBy(() -> ImportValidator.validateAudioImportFileExtensions(testFile, "This is an error message"))
                .withMessage("This is an error message");
    }
}
