package com.traderbuddyv2.core.util;

import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.FileSystems;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


/**
 * Testing class for {@link FileSystemUtils}
 *
 * @author Stephen Prizio
 * @version 1.0
 */
public class FileSystemUtilsTest {


    //  ----------------- getAudioFileUrl -----------------

    @Test
    public void test_getAudioFileUrl_absolute_success() {
        assertThat(FileSystemUtils.getAudioFileUrl(new MockMultipartFile("file", "hello.aac", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes()), true))
                .isEqualTo(FileSystems.getDefault().getPath("").toAbsolutePath() + "\\src\\main\\resources\\audio\\hello.aac");
    }

    @Test
    public void test_getAudioFileUrl_success() {
        assertThat(FileSystemUtils.getAudioFileUrl(new MockMultipartFile("file", "hello.aac", MediaType.TEXT_PLAIN_VALUE, "Hello, World!".getBytes()), false))
                .isEqualTo("\\src\\main\\resources\\audio\\hello.aac");
    }


    //  ----------------- getContentRoot -----------------

    @Test
    public void test_getContentRoot_absolute_success() {
        assertThat(FileSystemUtils.getContentRoot(true))
                .isEqualTo(FileSystems.getDefault().getPath("").toAbsolutePath() + "\\src\\main\\resources");
    }

    @Test
    public void test_getContentRoot_success() {
        assertThat(FileSystemUtils.getContentRoot(false))
                .isEqualTo("\\src\\main\\resources");
    }
}
