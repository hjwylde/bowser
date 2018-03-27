package com.hjwylde.bowser.io.file.archives;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ZipArchiveFileFactoryTests {
    private static final @NotNull ZipArchiveFileFactory FACTORY = new ZipArchiveFileFactory();

    private Path path = mock(Path.class);

    @Nested
    class CreateArchiveFile {
        @Test
        void returnsArchiveFile() {
            ZipArchiveFile archiveFile = FACTORY.createArchiveFile(path);

            assertNotNull(archiveFile);
        }

        @Test
        void returnsFalseWhenNullPath() {
            assertThrows(NullPointerException.class, () -> FACTORY.createArchiveFile(null));
        }
    }

    @Nested
    class IsSupportedContentType {
        @Test
        void returnsFalseWhenUnsupportedContentType() {
            List<String> unsupportedContentTypes = Arrays.asList("text/plain", "image/jpeg", "image/png");

            for (String unsupportedContentType : unsupportedContentTypes) {
                assertFalse(FACTORY.isSupportedContentType(unsupportedContentType));
            }
        }

        @Test
        void returnsTrueWhenSupportedContentType() {
            assertTrue(FACTORY.isSupportedContentType("application/zip"));
        }
    }
}
