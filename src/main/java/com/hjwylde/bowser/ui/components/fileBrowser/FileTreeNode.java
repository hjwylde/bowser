package com.hjwylde.bowser.ui.components.fileBrowser;

import org.jetbrains.annotations.NotNull;

import javax.swing.tree.DefaultMutableTreeNode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

final class FileTreeNode extends DefaultMutableTreeNode {
    // We are intentionally not using the parent's userObject as it is mutable
    private final @NotNull Path filePath;

    FileTreeNode(@NotNull Path filePath) {
        this.filePath = Objects.requireNonNull(filePath);
    }

    public @NotNull Path getFilePath() {
        return filePath;
    }

    @Override
    public boolean isLeaf() {
        return !Files.isDirectory(filePath);
    }

    @Override
    public String toString() {
        if (filePath.getFileName() != null) {
            return filePath.getFileName().toString();
        } else {
            // The fileName is null when dealing with a root directory
            return filePath.getRoot().toString();
        }
    }
}
