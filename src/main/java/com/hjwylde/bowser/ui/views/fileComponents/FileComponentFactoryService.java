package com.hjwylde.bowser.ui.views.fileComponents;

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
 * Uses the {@link ServiceLoader} pattern to load all visible {@link FileComponentFactory}s on the class path. Additional
 * {@link FileComponentFactory}s added will be automatically picked up.
 */
@Immutable
public final class FileComponentFactoryService {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(FileComponentFactoryService.class.getSimpleName());

    private static final @NotNull FileComponentFactoryService INSTANCE = new FileComponentFactoryService();

    private final @NotNull ServiceLoader<FileComponentFactory> loader;

    private FileComponentFactoryService() {
        loader = ServiceLoader.load(FileComponentFactory.class);
    }

    /**
     * Gets the singleton {@link FileComponentFactoryService} instance.
     *
     * @return the singleton instance.
     */
    public static @NotNull FileComponentFactoryService getInstance() {
        return INSTANCE;
    }

    /**
     * Gets a {@link FileComponentFactory} that supports the given file. Returns {@link Optional#empty()} if no
     * {@link FileComponentFactory} can be found.
     *
     * @param file the file to support.
     * @return a {@link FileComponentFactory} that supports the given file, or nothing.
     */
    public @NotNull Optional<FileComponentFactory> getFileComponentFactory(@NotNull Path file) {
        Optional<String> mContentType = getContentType(file);

        return mContentType.flatMap(
                contentType -> StreamSupport
                        .stream(loader.spliterator(), false)
                        .filter(factory -> factory.isSupportedContentType(contentType))
                        .findFirst());
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
