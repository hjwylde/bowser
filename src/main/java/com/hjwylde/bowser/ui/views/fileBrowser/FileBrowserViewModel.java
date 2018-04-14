package com.hjwylde.bowser.ui.views.fileBrowser;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.stream.Stream;

@Immutable
final class FileBrowserViewModel {
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
                throw new CompletionException(e);
            }
        });
    }
}
