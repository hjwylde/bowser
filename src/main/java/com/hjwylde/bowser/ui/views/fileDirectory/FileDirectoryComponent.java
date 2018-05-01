package com.hjwylde.bowser.ui.views.fileDirectory;

import com.hjwylde.bowser.ui.models.SortedListModel;
import com.hjwylde.bowser.util.concurrent.SwingExecutors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@NotThreadSafe
final class FileDirectoryComponent implements FileDirectory.View {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(FileDirectoryComponent.class.getSimpleName());

    private final @NotNull JList<FileNode> list;
    private final @NotNull SortedListModel<FileNode> listModel;

    private final @NotNull List<Consumer<Path>> directoryChangeListeners = new ArrayList<>();
    private final @NotNull List<Consumer<Optional<Path>>> selectedPathChangeListeners = new ArrayList<>();

    private final @NotNull FileDirectoryViewModel viewModel;

    private @NotNull Path directory;

    FileDirectoryComponent(@NotNull JList<FileNode> list, @NotNull SortedListModel<FileNode> listModel,
                           @NotNull Path startingPath, @NotNull FileDirectoryViewModel viewModel) {
        this.list = list;
        this.listModel = listModel;
        directory = startingPath;
        this.viewModel = viewModel;

        initialiseMouseListener();
        initialiseSelectedPathChangeListener();
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
    public void addSelectedPathChangeListener(@NotNull Consumer<Optional<Path>> listener) {
        selectedPathChangeListeners.add(listener);

        listener.accept(getSelectedPath());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JComponent getComponent() {
        return list;
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
    public @NotNull String getTitle() {
        if (directory.getFileName() == null) {
            return directory.toString();
        }

        return directory.getFileName().toString();
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void sort(@NotNull Comparator<Path> comparator) {
        listModel.setComparator((first, second) -> comparator.compare(first.getPath(), second.getPath()));
    }

    private @NotNull FileSystem getFileSystem() {
        return directory.getFileSystem();
    }

    private void initialiseActionMap() {
        // TODO (hjw): Cyclic references -> is it possible to avoid this?
        list.getActionMap().put(FileDirectoryAction.OPEN, new OpenAction(this));
    }

    private void initialiseInputMap() {
        list.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), FileDirectoryAction.OPEN);
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

                list.getActionMap().get(FileDirectoryAction.OPEN).actionPerformed(event);
            }
        });
    }

    private void initialiseSelectedPathChangeListener() {
        list.addListSelectionListener(e -> notifySelectedPathChangeListeners());
    }

    private void notifyDirectoryChangeListeners() {
        directoryChangeListeners.forEach(listener -> listener.accept(directory));
    }

    private void notifySelectedPathChangeListeners() {
        Optional<Path> mSelectedPath = getSelectedPath();

        selectedPathChangeListeners.forEach(listener -> listener.accept(mSelectedPath));
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

            paths.forEach(path -> listModel.add(new FileNode(path)));

            FileDirectoryComponent.this.directory = directory;
            notifyDirectoryChangeListeners();
        }
    }
}
