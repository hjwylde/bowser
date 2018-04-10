package com.hjwylde.bowser.ui.views.fileBrowser;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import javax.swing.tree.DefaultMutableTreeNode;
import java.nio.file.Files;
import java.nio.file.Path;

@Immutable
final class FileTreeNode extends DefaultMutableTreeNode {
    // We are intentionally not using the parent's userObject as it is mutable
    private final @NotNull Path filePath;

    FileTreeNode(@NotNull Path filePath) {
        this.filePath = filePath;
    }

    public @NotNull Path getFilePath() {
        return filePath;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isLeaf() {
        return !isDirectory();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        if (filePath.getFileName() != null) {
            return filePath.getFileName().toString();
        } else {
            // The fileName is null when dealing with a root directory
            return filePath.getRoot().toString();
        }
    }

    private boolean isDirectory() {
        return Files.isDirectory(filePath);
    }
}
