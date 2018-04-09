package com.hjwylde.bowser.ui.views.fileBrowser;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeExpansionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.util.Optional;

@NotThreadSafe
public final class FileBrowserComponent implements FileBrowser.View {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(FileBrowserComponent.class.getSimpleName());

    private final @NotNull JTree tree;
    private final @NotNull DefaultTreeModel treeModel;

    private final @NotNull FileBrowserViewModel viewModel;

    FileBrowserComponent(@NotNull JTree tree, @NotNull DefaultTreeModel treeModel, @NotNull FileBrowserViewModel viewModel) {
        this.tree = tree;
        this.treeModel = treeModel;
        this.viewModel = viewModel;

        initialiseListeners();
        initialiseInputMap();
        initialiseActionMap();
        initialiseRootNode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JComponent getComponent() {
        return tree;
    }

    @Override
    public Optional<Path> getSelectedPath() {
        Object selectedPathComponent = tree.getLastSelectedPathComponent();
        if (selectedPathComponent == null) {
            return Optional.empty();
        }

        FileTreeNode node = (FileTreeNode) selectedPathComponent;

        return Optional.of(node.getFilePath());
    }

    private void initialiseActionMap() {
        // TODO (hjw): Cyclic references -> is it possible to avoid this?
        tree.getActionMap().put(FileBrowserAction.OPEN, new OpenAction(this));
        tree.getActionMap().put(FileBrowserAction.PREVIEW, new PreviewAction(this));
    }

    private void initialiseInputMap() {
        tree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), FileBrowserAction.OPEN);
        tree.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), FileBrowserAction.PREVIEW);
    }

    private void initialiseListeners() {
        // TODO (hjw): How to refresh the directories if they change contents?
        tree.addTreeExpansionListener(new OnTreeExpansionListener());
    }

    private void initialiseRootNode() {
        DefaultMutableTreeNode node = (DefaultMutableTreeNode) treeModel.getRoot();

        viewModel.getRootDirectories()
                .subscribe(new OnGetChildrenObserver(node));
    }

    @NotThreadSafe
    private final class OnGetChildrenObserver implements Observer<Path> {
        private final @NotNull MutableTreeNode parent;

        private OnGetChildrenObserver(@NotNull MutableTreeNode parent) {
            this.parent = parent;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onComplete() {
            tree.expandPath(new TreePath(parent));
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onError(Throwable e) {
            LOGGER.warn(e.getMessage(), e);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onNext(Path path) {
            MutableTreeNode child = new FileTreeNode(path);
            int index = parent.getChildCount();

            treeModel.insertNodeInto(child, parent, index);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onSubscribe(Disposable d) {
            // TODO (hjw): How to detect when a component is destroyed? We wish to cancel any requests if the results
            // are to be ignored.
        }
    }

    @NotThreadSafe
    private final class OnTreeExpansionListener implements TreeExpansionListener {
        /**
         * {@inheritDoc}
         */
        @Override
        public void treeCollapsed(TreeExpansionEvent event) {
        }

        /**
         * {@inheritDoc}
         */
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
