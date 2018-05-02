package com.hjwylde.bowser.ui.views.fileDirectory;

import com.hjwylde.bowser.lang.util.Pair;
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
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
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
    public void sort(@NotNull Comparator<FileNode> comparator) {
        listModel.setComparator(comparator);
    }

    private void initialiseActionMap() {
        // TODO (hjw): Cyclic references -> is it possible to avoid this?
        list.getActionMap().put(FileDirectoryAction.OPEN, new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new OpenAction(FileDirectoryComponent.this).run();
            }
        });
    }

    private void initialiseInputMap() {
        list.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), FileDirectoryAction.OPEN);
    }

    private void initialiseMouseListener() {
        list.addMouseListener(new MouseAdapter() {
            private JPopupMenu popupMenu = new FileDirectoryPopupMenu(FileDirectoryComponent.this);

            /**
             * {@inheritDoc}
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && SwingUtilities.isLeftMouseButton(e)) {
                    doubleClick(e);
                } else if (e.getClickCount() == 1 && SwingUtilities.isRightMouseButton(e)) {
                    rightClick(e);
                }
            }

            private void doubleClick(MouseEvent e) {
                ActionEvent event = new ActionEvent(e, ActionEvent.ACTION_PERFORMED, "");

                list.getActionMap().get(FileDirectoryAction.OPEN).actionPerformed(event);
            }

            private void rightClick(MouseEvent e) {
                int index = list.locationToIndex(e.getPoint());
                if (index < 0) {
                    return;
                }

                list.setSelectedIndex(index);

                popupMenu.show(e.getComponent(), e.getX(), e.getY());
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
    private final class OnGetChildrenConsumer implements BiConsumer<List<Pair<Path, Optional<BasicFileAttributes>>>, Throwable> {
        private final @NotNull Path directory;

        OnGetChildrenConsumer(@NotNull Path directory) {
            this.directory = directory;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void accept(List<Pair<Path, Optional<BasicFileAttributes>>> pairs, Throwable throwable) {
            if (pairs != null) {
                onSuccess(pairs);
            } else if (throwable != null) {
                onError(throwable);
            }
        }

        private void onError(@NotNull Throwable throwable) {
            LOGGER.warn(throwable.getMessage(), throwable);

            // throwable is a CompletionException, let's handle the actual cause
            handleError(throwable.getCause());
        }

        private void onSuccess(@NotNull List<Pair<Path, Optional<BasicFileAttributes>>> pairs) {
            listModel.clear();
            pairs.forEach(pair -> {
                Path path = pair.getFirst();
                Optional<BasicFileAttributes> mAttributes = pair.getSecond();

                FileNode fileNode = mAttributes
                        .map(basicFileAttributes -> new FileNode(path, basicFileAttributes))
                        .orElseGet(() -> new FileNode(path));

                listModel.add(fileNode);
            });

            if (!pairs.isEmpty()) {
                list.setSelectedIndex(0);
                list.clearSelection();
            }

            FileDirectoryComponent.this.directory = directory;
            notifyDirectoryChangeListeners();
        }
    }
}
