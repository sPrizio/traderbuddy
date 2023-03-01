package com.traderbuddyv2.core.util;

import com.traderbuddyv2.core.constants.CoreConstants;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.FileSystems;

/**
 * Utility class for handling operations on the file system
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class FileSystemUtils {

    private FileSystemUtils() {
        throw new UnsupportedOperationException(CoreConstants.NO_INSTANTIATION);
    }


    /**
     * Returns a url for the given file that fits within this system's file system
     *
     * @param file {@link MultipartFile}
     * @return url
     */
    public static String getAudioFileUrl(final MultipartFile file, final boolean absolute) {

        if (absolute) {
            return FileSystems.getDefault().getPath("src/main/resources/audio/").toAbsolutePath() + "\\" + file.getOriginalFilename();
        }

        return "\\src\\main\\resources\\audio\\" + file.getOriginalFilename();
    }

    /**
     * Returns an absolute path to the content root
     *
     * @return string
     */
    public static String getContentRoot(final boolean absolute) {

        if (absolute) {
            return FileSystems.getDefault().getPath("src/main/resources").toAbsolutePath().toString();
        }

        return "\\src\\main\\resources";
    }
}
