package com.hjwylde.bowser.io.file.archives;

import org.jetbrains.annotations.NotNull;

import java.nio.file.Path;

/**
 * An abstract factory interface that creates {@link ArchiveFile}s from {@link Path}s. Subclasses should be thread safe.
 *
 * @param <E> the type of {@link ArchiveFile} this factory creates.
 */
public interface ArchiveFileFactory<E extends ArchiveFile> {
    /**
     * Creates an archive file for the given path. It is assumed that the path's content type is supported
     * ({@link #isSupportedContentType(String)}).
     *
     * @param path the archive file path.
     * @return a new archive file.
     * @throws NullPointerException if path is null.
     */
    @NotNull E createArchiveFile(@NotNull Path path);

    /**
     * Checks to see whether the given content type is supported by this factory. If this method returns true, it is
     * assumed that {@link #createArchiveFile(Path)} will be able to successfully create an {@link ArchiveFile}.
     *
     * @param contentType the content type to check.
     * @return true if the content type is supported.
     */
    boolean isSupportedContentType(@NotNull String contentType);
}
