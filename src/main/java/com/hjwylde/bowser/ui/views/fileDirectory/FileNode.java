package com.hjwylde.bowser.ui.views.fileDirectory;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.nio.file.Path;

@Immutable
final class FileNode {
    private final @NotNull Path path;

    FileNode(@NotNull Path path) {
        this.path = path;
    }

    public @NotNull Path getPath() {
        return path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return path.getFileName().toString();
    }
}
