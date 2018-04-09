package com.hjwylde.bowser.io.file.archives;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

/**
 * Uses the {@link ServiceLoader} pattern to load all visible {@link ArchiveFileFactory}s on the class path. Additional
 * {@link ArchiveFileFactory}s added will be automatically picked up.
 */
@Immutable
public final class ArchiveFileFactoryService {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(ArchiveFileFactoryService.class.getSimpleName());

    private static final @NotNull ArchiveFileFactoryService INSTANCE = new ArchiveFileFactoryService();

    private final @NotNull ServiceLoader<ArchiveFileFactory> loader;

    private ArchiveFileFactoryService() {
        loader = ServiceLoader.load(ArchiveFileFactory.class);
    }

    /**
     * Gets the singleton {@link ArchiveFileFactoryService} instance.
     *
     * @return the singleton instance.
     */
    public static @NotNull ArchiveFileFactoryService getInstance() {
        return INSTANCE;
    }

    /**
     * Gets an {@link ArchiveFileFactory} that supports the given archive file. Returns {@link Optional#empty()} if no
     * {@link ArchiveFileFactory} can be found.
     *
     * @param file the archive file to support.
     * @return an {@link ArchiveFileFactory} that supports the given file, or nothing.
     * @throws NullPointerException if file is null.
     */
    public @NotNull Optional<ArchiveFileFactory> getArchiveFileFactory(@NotNull Path file) {
        Optional<String> mContentType = getContentType(file);

        return mContentType.flatMap(
                contentType -> StreamSupport
                        .stream(loader.spliterator(), false)
                        .filter(factory -> factory.isSupportedContentType(contentType)
                        ).findFirst());
    }

    private @NotNull Optional<String> getContentType(@NotNull Path file) {
        try {
            return Optional.ofNullable(Files.probeContentType(file));
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);

            return Optional.empty();
        }
    }
}
