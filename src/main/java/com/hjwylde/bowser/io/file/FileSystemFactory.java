package com.hjwylde.bowser.io.file;

import org.jetbrains.annotations.NotNull;

import java.nio.file.FileSystem;

/**
 * An abstract interface into retrieving {@link FileSystem}s. Subclasses should be thread safe.
 */
public interface FileSystemFactory {
    /**
     * Gets a {@link FileSystem}. The returned file system may vary depending upon the subclass's implementation.
     *
     * @return a {@link FileSystem}.
     */
    @NotNull FileSystem getFileSystem();
}
