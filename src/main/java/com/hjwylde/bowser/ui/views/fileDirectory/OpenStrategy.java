package com.hjwylde.bowser.ui.views.fileDirectory;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

interface OpenStrategy {
    boolean isSupported(@NotNull Path path);

    void open(@NotNull Path path) throws IOException;
}
