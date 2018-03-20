package com.hjwylde.bowser.ui.components.fileBrowser;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.nio.file.FileSystem;
import java.util.Objects;

public final class FileBrowserBuilder {
    private FileSystem fileSystem;

    public FileBrowserBuilder() {
    }

    public @NotNull FileBrowserView build() {
        if (fileSystem == null) {
            throw new IllegalStateException("fileSystem must be set.");
        }

        TreeNode rootNode = new DefaultMutableTreeNode();
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        JTree tree = new JTree(treeModel);
        tree.setRootVisible(false);

        FileBrowserViewModel viewModel = new FileBrowserViewModel();

        return new FileBrowserView(tree, treeModel, viewModel, fileSystem);
    }

    public @NotNull FileBrowserBuilder fileSystem(@NotNull FileSystem fileSystem) {
        this.fileSystem = Objects.requireNonNull(fileSystem, "fileSystem cannot be null.");

        return this;
    }
}
