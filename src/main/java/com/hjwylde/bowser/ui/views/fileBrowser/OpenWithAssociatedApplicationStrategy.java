package com.hjwylde.bowser.ui.views.fileBrowser;

import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

final class OpenWithAssociatedApplicationStrategy implements OpenStrategy {
    @Override
    public boolean isSupported(@NotNull Path file) {
        // Let the file system try determine whether there is a default associated application for this file type.
        return true;
    }

    @Override
    public void openFile(@NotNull Path file) throws IOException {
        if (!Desktop.isDesktopSupported()) {
            throw new IOException("Desktop is not supported.");
        }

        // TODO (hjw): I don't think this will work with the FTP file system.
        Desktop.getDesktop().open(file.toFile());
    }
}
