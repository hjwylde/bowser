package com.hjwylde.bowser.ui.actions.open;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.nio.file.Path;

public interface OpenStrategy {
    boolean isSupported(@NotNull Path path);

    void open(@NotNull Path path) throws IOException;
}
