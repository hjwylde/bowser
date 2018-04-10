package com.hjwylde.bowser.ui.views.fileBrowser;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.nio.file.Files;
import java.nio.file.Path;

@NotThreadSafe
final class ChangeDirectoryStrategy implements OpenStrategy {
    private final @NotNull FileBrowser.View view;

    ChangeDirectoryStrategy(FileBrowser.View view) {
        this.view = view;
    }

    @Override
    public boolean isSupported(@NotNull Path path) {
        return Files.isDirectory(path);
    }

    @Override
    public void open(@NotNull Path path) {
        view.setDirectory(path);
    }
}
