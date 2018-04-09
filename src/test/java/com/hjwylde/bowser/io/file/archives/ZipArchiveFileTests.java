package com.hjwylde.bowser.io.file.archives;

import com.google.common.jimfs.Jimfs;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

class ZipArchiveFileTests {
    private static final @NotNull String ZIP_FILE_NAME = "test.zip";
    private static final @NotNull String ZIP_FILE_ENTRY_NAME = "content.txt";
    private static final @NotNull String ZIP_FILE_CONTENT = "test";

    private FileSystem fileSystem;
    private ZipArchiveFile file;

    @BeforeEach
    void init() throws IOException {
        fileSystem = Jimfs.newFileSystem();

        Path filePath = fileSystem.getPath("/", ZIP_FILE_NAME);
        createZipFile(filePath, ZIP_FILE_CONTENT.getBytes());

        file = new ZipArchiveFile(filePath);
    }

    @AfterEach
    void tearDown() throws IOException {
        fileSystem.close();
    }

    private void createZipFile(@NotNull Path file, @NotNull byte[] data) throws IOException {
        try (OutputStream out = Files.newOutputStream(file);
             ZipOutputStream zout = new ZipOutputStream(out)
        ) {
            ZipEntry e = new ZipEntry(ZIP_FILE_ENTRY_NAME);
            zout.putNextEntry(e);
            zout.write(data, 0, data.length);
            zout.closeEntry();
        }
    }

    @Nested
    class Extract {
        private Path destination;

        @Test
        void createsDestinationWhenAbsent() throws IOException {
            assumeTrue(!Files.exists(destination));

            file.extract(destination);

            assertTrue(Files.exists(destination));
        }

        @Test
        void extractsContents() throws IOException {
            file.extract(destination);

            DirectoryStream<Path> stream = Files.newDirectoryStream(destination);
            Path file = stream.iterator().next();

            assertEquals(destination.resolve(ZIP_FILE_ENTRY_NAME), file);
            assertEquals(ZIP_FILE_CONTENT, String.join("\n", Files.readAllLines(file)));
        }

        @BeforeEach
        void init() {
            destination = fileSystem.getPath("/", "destination/");
        }
    }
}
