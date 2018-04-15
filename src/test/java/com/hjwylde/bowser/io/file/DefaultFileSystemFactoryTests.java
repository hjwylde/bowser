package com.hjwylde.bowser.io.file;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DefaultFileSystemFactoryTests {
    @Nested
    class GetFileSystem {
        private final FileSystemFactory INSTANCE = DefaultFileSystemFactory.getInstance();

        @Test
        void returnsDefaultFileSystem() {
            FileSystem fileSystem = INSTANCE.getFileSystem();

            assertEquals(FileSystems.getDefault(), fileSystem);
        }
    }
}
