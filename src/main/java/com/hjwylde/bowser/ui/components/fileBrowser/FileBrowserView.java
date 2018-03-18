package com.hjwylde.bowser.ui.components.fileBrowser;

import com.hjwylde.bowser.ui.components.View;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.nio.file.Path;
import java.util.Objects;


public final class FileBrowserView implements View {
    private final static @NotNull Logger LOGGER = LogManager.getLogger(FileBrowserView.class.getSimpleName());

    private final @NotNull JTree tree;
    private final @NotNull DefaultTreeModel treeModel;

    private final @NotNull FileBrowserViewModel viewModel;

    FileBrowserView(@NotNull JTree tree, @NotNull DefaultTreeModel treeModel, @NotNull FileBrowserViewModel viewModel) {
        this.tree = Objects.requireNonNull(tree, "tree cannot be null.");
        this.treeModel = Objects.requireNonNull(treeModel, "treeModel cannot be null.");
        this.viewModel = Objects.requireNonNull(viewModel, "viewModel cannot be null.");

        initialiseListeners();
        initialiseRootNode();
    }

    @Override
    public @NotNull Component getComponent() {
        return tree;
    }

    private void initialiseListeners() {
        // TODO (hjw): How to refresh the directories if they change contents?
        tree.addTreeExpansionListener(new OnTreeExpansionListener());
    }

    private void initialiseRootNode() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeModel.getRoot();

        viewModel.getRootDirectories().subscribe(new OnGetChildrenObserver(node));
    }

    private final class OnGetChildrenObserver implements Observer<Path> {
        private final @NotNull MutableTreeNode parent;

        private OnGetChildrenObserver(@NotNull MutableTreeNode parent) {
            this.parent = Objects.requireNonNull(parent, "parent cannot be null.");
        }

        @Override
        public void onComplete() {
            tree.expandPath(new TreePath(parent));
        }

        @Override
        public void onError(Throwable e) {
            LOGGER.warn(e);
        }

        @Override
        public void onNext(Path path) {
            MutableTreeNode child = new FileTreeNode(path);
            int index = parent.getChildCount();

            treeModel.insertNodeInto(child, parent, index);
        }

        @Override
        public void onSubscribe(Disposable d) {
            // TODO (hjw): How to detect when a component is destroyed? We wish to cancel any requests if the results
            // are to be ignored.
        }
    }

    private final class OnTreeExpansionListener implements TreeExpansionListener {
        @Override
        public void treeCollapsed(TreeExpansionEvent event) {
        }

        @Override
        public void treeExpanded(TreeExpansionEvent event) {
            TreePath treePath = event.getPath();
            if (!(treePath.getLastPathComponent() instanceof FileTreeNode)) {
                // This is probably the root node, either way, nothing for us to do here
                return;
            }

            FileTreeNode node = (FileTreeNode) treePath.getLastPathComponent();
            if (isPopulated(node)) {
                // It's worth noting that this will not deal with file system refreshes. I have a bigger idea about how
                // to properly handle this, specifically using Subjects. This would also allow for the view model to be
                // re-used between file browsers to be more efficient.
                return;
            }

            Path path = node.getFilePath();

            viewModel.getChildren(path).subscribe(new OnGetChildrenObserver(node));
        }

        private boolean isPopulated(FileTreeNode node) {
            return node.isLeaf() || node.getChildCount() > 0;
        }

    }
}
