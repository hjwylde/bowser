package com.hjwylde.bowser.ui.views.fileBrowser;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

@Immutable
final class OpenWithAssociatedApplicationStrategy implements OpenStrategy {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSupported(@NotNull Path file) {
        // Let the file system try determine whether there is a default associated application for this file type.
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void openFile(@NotNull Path file) throws IOException {
        if (!Desktop.isDesktopSupported()) {
            throw new IOException("Desktop is not supported.");
        }

        // TODO (hjw): I don't think this will work with the FTP file system.
        Desktop.getDesktop().open(file.toFile());
    }
}
