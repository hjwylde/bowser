package com.hjwylde.bowser.ui.views.fileBrowser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@NotThreadSafe
final class BrowseArchiveStrategy implements OpenStrategy {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(FileBrowserComponent.class.getSimpleName());

    private static final @NotNull List<String> BROWSABLE_CONTENT_TYPES = Arrays.asList(
            "application/java-archive",
            "application/zip"
    );

    private final @NotNull FileBrowser.View view;

    BrowseArchiveStrategy(@NotNull FileBrowser.View view) {
        this.view = view;
    }

    @Override
    public boolean isSupported(@NotNull Path path) {
        if (Files.isDirectory(path)) {
            return false;
        }

        return getContentType(path)
                .filter(BROWSABLE_CONTENT_TYPES::contains)
                .isPresent();
    }

    @Override
    public void open(@NotNull Path path) throws IOException {
        // TODO (hjw): A file system should be closed when finished with, somehow I need to ensure that we close this
        // one. I have an idea about how to do this: I would like to store more information about the current state and
        // history in the FileBrowserComponent. When going back in the history (exiting the archive browser) it would be
        // possible to close the file system. Likewise, when closing a tab it should close the file system if it's not
        // being used by another tab.
        FileSystem fileSystem = getFileSystem(path);
        Path fileSystemPath = getRootDirectory(fileSystem);

        view.setDirectory(fileSystemPath);
    }

    private @NotNull Optional<String> getContentType(@NotNull Path path) {
        try {
            return Optional.ofNullable(Files.probeContentType(path));
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);

            return Optional.empty();
        }
    }

    private @NotNull FileSystem getFileSystem(@NotNull Path path) throws IOException {
        // ZipFileSystemProvider supports browsing archives in the default file system, so let's try utilise that first.
        Path archive = path;

        // See ZipFileSystemProvider#newFileSystem(Path, Map<String, ?>))
        if (path.getFileSystem() != FileSystems.getDefault()) {
            // Okay, so it looks like ZipFileSystemProvider won't be able to support this archive (perhaps it's nested,
            // or perhaps it's on some other type of file system (such as FTP). We now need to temporarily extract the
            // archive to the default file system so that we may browse it.
            Path tempDirectory = Files.createTempDirectory(null);
            archive = tempDirectory.resolve(path.getFileName().toString());

            Files.copy(path, archive);
        }

        return FileSystems.newFileSystem(archive, null);
    }

    private @NotNull Path getRootDirectory(@NotNull FileSystem fileSystem) throws IOException {
        Iterator<Path> it = fileSystem.getRootDirectories().iterator();
        if (!it.hasNext()) {
            throw new IOException("Unable to browse archive file, please check to see if you have sufficient permissions.");
        }

        // There should only ever be one root directory available for a Zip file system.
        return it.next();
    }
}
