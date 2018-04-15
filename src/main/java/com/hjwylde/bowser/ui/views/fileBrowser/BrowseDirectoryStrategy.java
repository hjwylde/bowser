package com.hjwylde.bowser.ui.views.fileBrowser;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.nio.file.Files;
import java.nio.file.Path;

@NotThreadSafe
final class BrowseDirectoryStrategy implements OpenStrategy {
    private final @NotNull FileBrowser.View view;

    BrowseDirectoryStrategy(@NotNull FileBrowser.View view) {
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSupported(@NotNull Path path) {
        return Files.isDirectory(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open(@NotNull Path path) {
        view.setDirectory(path);
    }
}
