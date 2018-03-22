package com.hjwylde.bowser.io.file.archives;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

/**
 * An abstract archive file. This interface is used to provide helpful methods when interacting with an archive file.
 * It's important that all subclasses are fully compatible with Java's {@link java.nio.file.FileSystem} pattern. For
 * example, a ZIP archive should not use {@link java.util.zip.ZipFile} as it uses the old {@link java.io.File} pattern.
 */
public interface ArchiveFile {
    /**
     * Extracts this archive to the given destination. If the destination does not exist, it will be created.
     *
     * @param destination the destination to unarchive this file.
     * @throws IOException          if any problems occur with reading the archive, or writing to the destination.
     * @throws NullPointerException if destination is null.
     */
    void extract(@NotNull Path destination) throws IOException;
}
