package com.hjwylde.bowser.io.file.archives;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * An {@link ArchiveFile} that supports the ZIP file format.
 */
final class ZipArchiveFile implements ArchiveFile {
    private final Path path;

    ZipArchiveFile(@NotNull Path path) {
        this.path = Objects.requireNonNull(path, "path cannot be null.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void extract(@NotNull Path destination) throws IOException {
        if (!Files.exists(destination)) {
            Files.createDirectories(destination);
        }

        try (InputStream in = Files.newInputStream(path);
             BufferedInputStream bin = new BufferedInputStream(in);
             ZipInputStream zin = new ZipInputStream(bin)
        ) {
            for (ZipEntry entry = zin.getNextEntry(); entry != null; entry = zin.getNextEntry()) {
                Path entryPath = destination.resolve(entry.getName());

                Path parent = entry.isDirectory() ? entryPath : entryPath.getParent();
                if (!Files.exists(parent)) {
                    Files.createDirectories(parent);
                }

                if (!entry.isDirectory()) {
                    Files.copy(zin, entryPath);
                }

                zin.closeEntry();
            }
        }
    }
}
