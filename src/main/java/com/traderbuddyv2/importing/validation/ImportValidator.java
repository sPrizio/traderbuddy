package com.traderbuddyv2.importing.validation;

import com.traderbuddyv2.core.constants.CoreConstants;
import com.traderbuddyv2.importing.exceptions.FileExtensionNotSupportedException;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Arrays;

/**
 * Validator class for the import package
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class ImportValidator {

    private ImportValidator() {
        throw new UnsupportedOperationException(String.format(CoreConstants.NO_INSTANTIATION, getClass().getName()));
    }

    /**
     * Validates that the given {@link MultipartFile}'s extension matches the expected one
     *
     * @param file               {@link MultipartFile}
     * @param expectedExtensions expected extension (.docx, .txt, etc...)
     * @param message            error message
     * @param values             error message values
     * @throws FileExtensionNotSupportedException if file extension is not supported
     */
    public static void validateImportFileExtension(final MultipartFile file, final String[] expectedExtensions, final String message, final Object... values) {

        if (expectedExtensions != null) {
            Arrays.stream(expectedExtensions).filter(expectedExtension -> FilenameUtils.getExtension(file.getOriginalFilename()).equals(expectedExtension.replace(".", ""))).findFirst().orElseThrow(() -> new FileExtensionNotSupportedException(String.format(message, values)));
        }
    }

    /**
     * Validates that the given {@link File}'s extension matches the expected one
     *
     * @param file               {@link File}
     * @param expectedExtensions expected extension (.docx, .txt, etc...)
     * @param message            error message
     * @param values             error message values
     * @throws FileExtensionNotSupportedException is file extension is not supported
     */
    public static void validateImportFileExtension(final File file, final String[] expectedExtensions, final String message, final Object... values) {

        if (expectedExtensions != null) {
            Arrays.stream(expectedExtensions).filter(expectedExtension -> FilenameUtils.getExtension(file.getName()).equals(expectedExtension.replace(".", ""))).findFirst().orElseThrow(() -> new FileExtensionNotSupportedException(String.format(message, values)));
        }
    }

    /**
     * Validates that the given {@link MultipartFile}'s extensions matches a supported one
     *
     * @param file    {@link MultipartFile}
     * @param message error message
     * @param values  error message values
     */
    public static void validateAudioImportFileExtensions(final MultipartFile file, final String message, final Object... values) {
        if (!CoreConstants.SUPPORTED_AUDIO_FORMATS.contains(FilenameUtils.getExtension(file.getOriginalFilename()))) {
            throw new FileExtensionNotSupportedException(String.format(message, values));
        }
    }
}
