package com.hjwylde.bowser.ui.views.fileBrowser;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

interface OpenStrategy {
    boolean isSupported(@NotNull Path file);

    void openFile(@NotNull Path file) throws IOException;
}
