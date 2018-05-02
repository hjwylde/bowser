package com.hjwylde.bowser.ui.views.fileDirectory;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Comparator;

@Immutable
public final class FileNode implements Comparable<FileNode> {
    public static final @NotNull Comparator<FileNode> NAME_COMPARATOR = Comparator.comparing(FileNode::getFileName);
    public static final @NotNull Comparator<FileNode> SIZE_COMPARATOR = Comparator.comparing(FileNode::getFileSize);

    private final @NotNull Path path;
    private final BasicFileAttributes attributes;

    FileNode(@NotNull Path path) {
        this(path, null);
    }

    FileNode(@NotNull Path path, BasicFileAttributes attributes) {
        this.path = path;
        this.attributes = attributes;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int compareTo(@NotNull FileNode other) {
        return NAME_COMPARATOR.compare(this, other);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof FileNode)) {
            return false;
        }

        FileNode fileNode = (FileNode) obj;

        return path.equals(fileNode.path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return path.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return path.getFileName().toString();
    }

    @NotNull Path getPath() {
        return path;
    }

    private Path getFileName() {
        return path.getFileName();
    }

    private long getFileSize() {
        if (attributes == null) {
            return -1L;
        }

        return attributes.size();
    }
}
