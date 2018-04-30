package com.hjwylde.bowser.ui.views.fileDirectory;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.nio.file.Path;
import java.util.Comparator;

@Immutable
final class FileNode implements Comparable<FileNode> {
    private final @NotNull Path path;

    FileNode(@NotNull Path path) {
        this.path = path;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(@NotNull FileNode other) {
        return Comparator.comparing(Path::getFileName).compare(path, other.path);
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
