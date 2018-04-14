package com.hjwylde.bowser.ui.views.tabbedFileBrowser;

import com.hjwylde.bowser.modules.LocaleModule;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@Immutable
final class TabbedFileBrowserViewModel {
    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(TabbedFileBrowserViewModel.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_ERROR_NO_STARTING_PATH = "errorNoStartingPath";

    private static final @NotNull String USER_HOME = System.getProperty("user.home");

    TabbedFileBrowserViewModel() {
    }

    @NotNull CompletableFuture<Path> selectStartingPath(@NotNull FileSystem fileSystem) {
        return CompletableFuture.supplyAsync(() -> {
            // Default to the user home directory first
            Path path = fileSystem.getPath(USER_HOME);
            if (Files.exists(path)) {
                return path;
            }

            Iterator<Path> it = fileSystem.getRootDirectories().iterator();
            if (!it.hasNext()) {
                Exception e = new IOException(RESOURCES.getString(RESOURCE_ERROR_NO_STARTING_PATH));

                throw new CompletionException(e);
            }

            // Else, display the first root directory available
            // It's unlikely that there will be more than one root directory available. Windows is the only common situation
            // that this occurs in, and for that scenario we'd expect the USER_HOME path to exist.
            return it.next();
        });
    }
}
