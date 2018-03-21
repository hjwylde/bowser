package com.hjwylde.bowser.services;

import com.hjwylde.bowser.io.file.archives.ArchiveFileFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.StreamSupport;

public final class ArchiveFileFactoryService {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(ArchiveFileFactoryService.class.getSimpleName());

    private static final @NotNull ArchiveFileFactoryService INSTANCE = new ArchiveFileFactoryService();

    private final @NotNull ServiceLoader<ArchiveFileFactory> loader;

    private ArchiveFileFactoryService() {
        loader = ServiceLoader.load(ArchiveFileFactory.class);
    }

    public static @NotNull ArchiveFileFactoryService getInstance() {
        return INSTANCE;
    }

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
