package com.hjwylde.bowser.ui.views.fileBrowser;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Immutable
final class OpenWithAssociatedApplicationStrategy implements OpenStrategy {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSupported(@NotNull Path path) {
        // Let the file system try determine whether there is a default associated application for this file type.
        return !Files.isDirectory(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open(@NotNull Path path) throws IOException {
        if (!Desktop.isDesktopSupported()) {
            throw new IOException("Desktop is not supported.");
        }

        // TODO (hjw): I don't think this will work with the FTP file system.
        Desktop.getDesktop().open(path.toFile());
    }
}
