package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.ui.views.filePreview.FilePreview;
import com.hjwylde.bowser.util.concurrent.SwingExecutors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@NotThreadSafe
final class FileBrowserComponent implements FileBrowser.View {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(FileBrowserComponent.class.getSimpleName());

    private final @NotNull JComponent root;
    private final @NotNull JList<FileNode> list;
    private final @NotNull DefaultListModel<FileNode> listModel;

    private final @NotNull FilePreview.View filePreviewView;

    private final @NotNull List<Consumer<Path>> directoryChangeListeners = new ArrayList<>();

    private final @NotNull FileBrowserViewModel viewModel;

    private @NotNull Path directory;

    // TODO (hjw): There are a lot of arguments here, and this class is now responsible for dealing with updating the
    // browser and preview. Perhaps there is a parent concept that could be factored out to separate out the concerns?
    FileBrowserComponent(@NotNull JComponent root, @NotNull JList<FileNode> list,
                         @NotNull DefaultListModel<FileNode> listModel, @NotNull FilePreview.View filePreviewView,
                         @NotNull Path startingPath, @NotNull FileBrowserViewModel viewModel) {
        this.root = root;
        this.list = list;
        this.listModel = listModel;
        this.filePreviewView = filePreviewView;
        directory = startingPath;
        this.viewModel = viewModel;

        initialiseMouseListener();
        initialiseFilePreviewListener();
        initialiseInputMap();
        initialiseActionMap();

        setDirectory(directory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDirectoryChangeListener(@NotNull Consumer<Path> listener) {
        directoryChangeListeners.add(listener);

        listener.accept(directory);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JComponent getComponent() {
        return root;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Path getDirectory() {
        return directory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Optional<Path> getSelectedPath() {
        FileNode fileNode = list.getSelectedValue();
        if (fileNode == null) {
            return Optional.empty();
        }

        return Optional.of(fileNode.getPath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDirectory(@NotNull Path directory) {
        OnGetChildrenConsumer handler = new OnGetChildrenConsumer(directory);

        viewModel.getChildren(directory)
                .whenCompleteAsync(handler, SwingExecutors.edt());
    }

    private void initialiseActionMap() {
        // TODO (hjw): Cyclic references -> is it possible to avoid this?
        list.getActionMap().put(FileBrowserAction.OPEN, new OpenAction(this));
        list.getActionMap().put(FileBrowserAction.PREVIOUS, new PreviousAction(this));
    }

    private void initialiseFilePreviewListener() {
        list.addListSelectionListener(new OnListSelectionChangeListener());
    }

    private void initialiseInputMap() {
        list.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), FileBrowserAction.OPEN);
        list.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), FileBrowserAction.PREVIOUS);
    }

    private void initialiseMouseListener() {
        list.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() != 2) {
                    return;
                }

                ActionEvent event = new ActionEvent(e, ActionEvent.ACTION_PERFORMED, "");

                list.getActionMap().get(FileBrowserAction.OPEN).actionPerformed(event);
            }
        });
    }

    private void notifyDirectoryChangeListeners() {
        directoryChangeListeners.forEach(listener -> listener.accept(directory));
    }

    @NotThreadSafe
    private final class OnGetChildrenConsumer implements BiConsumer<List<Path>, Throwable> {
        private final @NotNull Path directory;

        OnGetChildrenConsumer(@NotNull Path directory) {
            this.directory = directory;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void accept(List<Path> paths, Throwable throwable) {
            if (paths != null) {
                onSuccess(paths);
            } else if (throwable != null) {
                onError(throwable);
            }
        }

        private void onError(@NotNull Throwable throwable) {
            LOGGER.warn(throwable.getMessage(), throwable);

            // throwable is a CompletionException, let's handle the actual cause
            handleError(throwable.getCause());
        }

        private void onSuccess(@NotNull List<Path> paths) {
            listModel.clear();

            paths.forEach(path -> listModel.addElement(new FileNode(path)));

            FileBrowserComponent.this.directory = directory;
            notifyDirectoryChangeListeners();
        }
    }

    @NotThreadSafe
    private final class OnListSelectionChangeListener implements ListSelectionListener {
        /**
         * {@inheritDoc}
         */
        @Override
        public void valueChanged(ListSelectionEvent e) {
            JList list = (JList) e.getSource();

            FileNode fileNode = (FileNode) list.getSelectedValue();
            if (fileNode != null) {
                filePreviewView.setFile(fileNode.getPath());
            } else {
                filePreviewView.clearFile();
            }
        }
    }
}
