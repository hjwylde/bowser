package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.io.file.RxFileSystem;
import com.hjwylde.bowser.io.file.RxFiles;
import com.hjwylde.bowser.modules.RxFilesModule;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.Optional;

/**
 * {@link FileBrowser} provides the interfaces to create and use a component that displays a file tree. The file tree is
 * navigatable and interactable. E.g., it is possible to extract and open different file types.
 */
public final class FileBrowser {
    private FileBrowser() {
    }

    /**
     * Creates a new {@link Builder}.
     *
     * @return a new {@link Builder}.
     */
    public static @NotNull Builder builder() {
        return new Builder();
    }

    public interface View extends com.hjwylde.bowser.ui.views.View {
        /**
         * Gets the currently selected path. If no path is selected, then {@link Optional#empty()} is returned.
         *
         * @return the currently selected path, or {@link Optional#empty()}.
         */
        Optional<Path> getSelectedPath();
    }

    @NotThreadSafe
    public static final class Builder {
        private FileSystem fileSystem;

        private Builder() {
        }

        /**
         * Builds and returns a new {@link FileBrowser.View}. The file browser view must have a file system to
         * reference.
         *
         * @return a new {@link FileBrowser.View}.
         * @throws IllegalStateException if fileSystem is null.
         */
        public @NotNull View build() {
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

            return new FileBrowserComponent(tree, treeModel, viewModel);
        }

        public @NotNull Builder fileSystem(@NotNull FileSystem fileSystem) {
            this.fileSystem = fileSystem;

            return this;
        }
    }
}
