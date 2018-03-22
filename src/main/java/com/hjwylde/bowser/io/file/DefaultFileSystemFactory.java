package com.hjwylde.bowser.io.file;

import org.jetbrains.annotations.NotNull;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

/**
 * A default implementation of a {@link FileSystemFactory}. This implementation simply returns the default
 * {@link FileSystem}.
 *
 * @see FileSystems#getDefault()
 */
public final class DefaultFileSystemFactory implements FileSystemFactory {
    private static final @NotNull FileSystemFactory INSTANCE = new DefaultFileSystemFactory();

    private DefaultFileSystemFactory() {
    }

    /**
     * Gets the singleton {@link DefaultFileSystemFactory} instance. This instance is thread safe.
     *
     * @return the singleton instance.
     */
    public static @NotNull FileSystemFactory getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull FileSystem getFileSystem() {
        return FileSystems.getDefault();
    }
}
