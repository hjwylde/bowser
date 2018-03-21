package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.io.RxFileSystem;
import com.hjwylde.bowser.io.RxFiles;
import com.hjwylde.bowser.modules.RxFilesModule;
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

        RxFiles rxFiles = RxFilesModule.provideRxFiles();
        RxFileSystem rxFileSystem = RxFileSystem.forFileSystem(fileSystem);
        FileBrowserViewModel viewModel = new FileBrowserViewModel(rxFiles, rxFileSystem);

        return new FileBrowserView(tree, treeModel, viewModel);
    }

    public @NotNull FileBrowserBuilder fileSystem(@NotNull FileSystem fileSystem) {
        this.fileSystem = Objects.requireNonNull(fileSystem, "fileSystem cannot be null.");

        return this;
    }
}
