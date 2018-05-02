package com.hjwylde.bowser.ui.actions.open;

import com.hjwylde.bowser.ui.views.fileDirectory.FileDirectory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.nio.file.Files;
import java.nio.file.Path;

@NotThreadSafe
public final class BrowseDirectoryStrategy implements OpenStrategy {
    private final @NotNull FileDirectory.View view;

    public BrowseDirectoryStrategy(@NotNull FileDirectory.View view) {
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
