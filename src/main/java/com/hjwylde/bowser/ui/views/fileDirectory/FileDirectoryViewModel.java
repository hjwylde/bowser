package com.hjwylde.bowser.ui.views.fileDirectory;

import com.hjwylde.bowser.modules.LocaleModule;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Immutable
final class FileDirectoryViewModel {
    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(FileDirectoryViewModel.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_ERROR_BROWSING_PATH = "errorBrowsingPath";

    private static final @NotNull Predicate<Path> VISIBLE_FILE_FILTER = path -> {
        // If we're browsing a non-default file system, we probably want to list all files. E.g., a user would be
        // interested in all files from the FTP server or ZIP archive. This is handy for us, as Files.isHidden(Path) is
        // quite expensive depending on the file system.
        if (path.getFileSystem() != FileSystems.getDefault()) {
            return true;
        }

        try {
            return !Files.isHidden(path);
        } catch (IOException ignored) {
            // If we can't determine the visibility of the path, assume that it's an attributes issue and still list it
            return true;
        }
    };

    FileDirectoryViewModel() {
    }

    @NotNull CompletableFuture<List<Path>> getChildren(@NotNull Path parent) {
        return CompletableFuture.supplyAsync(() -> {
            try (Stream<Path> paths = Files.list(parent)) {
                return paths
                        .filter(VISIBLE_FILE_FILTER)
                        .sorted()
                        .collect(Collectors.toList());
            } catch (IOException e) {
                IOException e2 = new IOException(RESOURCES.getString(RESOURCE_ERROR_BROWSING_PATH), e);

                throw new CompletionException(e2);
            }
        });
    }
}
