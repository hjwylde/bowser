package com.hjwylde.bowser.ui.components.fileBrowser;

import com.hjwylde.bowser.fileBrowsers.FileBrowser;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.util.Objects;

public final class FileBrowserBuilder {
    private FileBrowser fileBrowser;

    public FileBrowserBuilder() {
    }

    public @NotNull FileBrowserView build() {
        if (fileBrowser == null) {
            throw new IllegalStateException("fileBrowser must be set.");
        }

        TreeNode rootNode = new DefaultMutableTreeNode();
        DefaultTreeModel treeModel = new DefaultTreeModel(rootNode);
        JTree tree = new JTree(treeModel);
        tree.setRootVisible(false);

        FileBrowserViewModel viewModel = new FileBrowserViewModel(fileBrowser);

        return new FileBrowserView(tree, treeModel, viewModel);
    }

    public @NotNull FileBrowserBuilder fileBrowser(@NotNull FileBrowser fileBrowser) {
        this.fileBrowser = Objects.requireNonNull(fileBrowser, "fileBrowser cannot be null.");

        return this;
    }
}
