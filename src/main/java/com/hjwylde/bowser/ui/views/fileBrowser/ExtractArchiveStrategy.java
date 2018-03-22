package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.io.file.archives.ArchiveFile;
import com.hjwylde.bowser.io.file.archives.ArchiveFileFactory;
import com.hjwylde.bowser.services.ArchiveFileFactoryService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;

final class ExtractArchiveStrategy implements OpenStrategy {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(ExtractArchiveStrategy.class.getSimpleName());

    private final @NotNull ArchiveFileFactoryService archiveFileFactoryService;

    ExtractArchiveStrategy() {
        this(ArchiveFileFactoryService.getInstance());
    }

    ExtractArchiveStrategy(@NotNull ArchiveFileFactoryService archiveFileFactoryService) {
        this.archiveFileFactoryService = Objects.requireNonNull(archiveFileFactoryService, "archiveFileFactoryService cannot be null.");
    }

    @Override
    public boolean isSupported(@NotNull Path file) {
        return getArchiveFileFactory(file).isPresent();
    }

    @Override
    public void openFile(@NotNull Path file) throws IOException {
        Optional<ArchiveFileFactory> mArchiveFileFactory = getArchiveFileFactory(file);
        if (!mArchiveFileFactory.isPresent()) {
            throw new IllegalArgumentException("archive is not supported for extraction.");
        }

        ArchiveFileFactory archiveFileFactory = mArchiveFileFactory.get();
        ArchiveFile archiveFile = archiveFileFactory.createArchiveFile(file);

        // Using the archive parent path as the directory name is less than ideal, it would be nicer to ask the user
        // where to expand the archive
        archiveFile.extract(file.getParent());
    }

    private @NotNull Optional<ArchiveFileFactory> getArchiveFileFactory(@NotNull Path file) {
        return archiveFileFactoryService.getArchiveFileFactory(file);
    }
}
