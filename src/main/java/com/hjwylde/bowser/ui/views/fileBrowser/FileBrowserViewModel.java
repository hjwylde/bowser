package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.modules.LocaleModule;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Stream;

@Immutable
final class FileBrowserViewModel {
    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(FileBrowserViewModel.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_ERROR_BROWSING_PATH = "errorBrowsingPath";

    FileBrowserViewModel() {
    }

    public @NotNull CompletableFuture<Stream<Path>> getChildren(@NotNull Path parent) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return Files.list(parent)
                        .filter(child -> {
                            try {
                                return !Files.isHidden(child);
                            } catch (IOException e) {
                                return false;
                            }
                        })
                        .sorted();
            } catch (IOException e) {
                IOException e2 = new IOException(RESOURCES.getString(RESOURCE_ERROR_BROWSING_PATH), e);

                throw new CompletionException(e2);
            }
        });
    }
}
