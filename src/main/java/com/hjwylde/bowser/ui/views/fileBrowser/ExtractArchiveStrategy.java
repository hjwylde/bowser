package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.io.file.archives.ArchiveFile;
import com.hjwylde.bowser.io.file.archives.ArchiveFileFactory;
import com.hjwylde.bowser.io.file.archives.ArchiveFileFactoryService;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Immutable
final class ExtractArchiveStrategy implements OpenStrategy {
    private final @NotNull ArchiveFileFactoryService archiveFileFactoryService = ArchiveFileFactoryService.getInstance();

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSupported(@NotNull Path path) {
        return !Files.isDirectory(path) && getArchiveFileFactory(path).isPresent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open(@NotNull Path path) throws IOException {
        Optional<ArchiveFileFactory> mArchiveFileFactory = getArchiveFileFactory(path);
        if (!mArchiveFileFactory.isPresent()) {
            throw new IllegalArgumentException("archive is not supported for extraction.");
        }

        ArchiveFileFactory archiveFileFactory = mArchiveFileFactory.get();
        ArchiveFile archiveFile = archiveFileFactory.createArchiveFile(path);

        // Using the archive parent path as the directory name is less than ideal, it would be nicer to ask the user
        // where to expand the archive
        archiveFile.extract(path.getParent());
    }

    private @NotNull Optional<ArchiveFileFactory> getArchiveFileFactory(@NotNull Path file) {
        return archiveFileFactoryService.getArchiveFileFactory(file);
    }
}
