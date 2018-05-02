package com.hjwylde.bowser.ui.views.fileDirectory;

import com.hjwylde.bowser.modules.LocaleModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

@NotThreadSafe
final class BrowseArchiveStrategy implements OpenStrategy {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(FileDirectoryComponent.class.getSimpleName());

    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(BrowseArchiveStrategy.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_ERROR = "error";
    private static final @NotNull String RESOURCE_ERROR_NO_STARTING_PATH = "errorNoStartingPath";

    private static final @NotNull List<String> BROWSABLE_CONTENT_TYPES = Arrays.asList(
            "application/java-archive",
            "application/zip"
    );

    private final @NotNull FileDirectory.View view;

    BrowseArchiveStrategy(@NotNull FileDirectory.View view) {
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSupported(@NotNull Path path) {
        return !Files.isDirectory(path) &&
                getContentType(path).filter(BROWSABLE_CONTENT_TYPES::contains).isPresent();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open(@NotNull Path path) throws IOException {
        // TODO (hjw): A file system should be closed when finished with, somehow I need to ensure that we close this
        // one. I have an idea about how to do this: I would like to store more information about the current state and
        // history in the FileDirectoryComponent. When going back in the history (exiting the archive browser) it would be
        // possible to close the file system. Likewise, when closing a tab it should close the file system if it's not
        // being used by another tab.
        try {
            FileSystem fileSystem = getFileSystem(path);
            Path fileSystemPath = getRootDirectory(fileSystem);

            view.setDirectory(fileSystemPath);
        } catch (IOException e) {
            throw new IOException(RESOURCES.getString(RESOURCE_ERROR), e);
        }
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

            try (InputStream in = Files.newInputStream(path)) {
                Files.copy(in, archive);
            }
        }

        return FileSystems.newFileSystem(archive, null);
    }

    private @NotNull Path getRootDirectory(@NotNull FileSystem fileSystem) throws IOException {
        Iterator<Path> it = fileSystem.getRootDirectories().iterator();
        if (!it.hasNext()) {
            throw new IOException(RESOURCES.getString(RESOURCE_ERROR_NO_STARTING_PATH));
        }

        // There should only ever be one root directory available for a Zip file system.
        return it.next();
    }
}
