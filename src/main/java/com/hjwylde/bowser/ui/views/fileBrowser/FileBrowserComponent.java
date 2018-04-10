package com.hjwylde.bowser.ui.views.fileBrowser;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

@NotThreadSafe
public final class FileBrowserComponent implements FileBrowser.View {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(FileBrowserComponent.class.getSimpleName());

    private final @NotNull DefaultListModel<FileNode> listModel = new DefaultListModel<>();
    private final @NotNull JList<FileNode> list = new JList<>(listModel);

    private final @NotNull List<Consumer<Path>> directoryChangeListeners = new ArrayList<>();

    private final @NotNull FileBrowserViewModel viewModel;

    private @NotNull Path directory;

    FileBrowserComponent(@NotNull Path startingPath, @NotNull FileBrowserViewModel viewModel) {
        directory = startingPath;
        this.viewModel = viewModel;

        initialiseMouseListener();
        initialiseInputMap();
        initialiseActionMap();

        setDirectory(directory);
    }

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
        return list;
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

    @Override
    public void setDirectory(@NotNull Path directory) {
        viewModel.getChildren(directory)
                .subscribe(new OnGetChildrenObserver(directory));
    }

    private void initialiseActionMap() {
        // TODO (hjw): Cyclic references -> is it possible to avoid this?
        list.getActionMap().put(FileBrowserAction.OPEN, new OpenAction(this));
        list.getActionMap().put(FileBrowserAction.PREVIEW, new PreviewAction(this));
    }

    private void initialiseInputMap() {
        list.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), FileBrowserAction.OPEN);
        list.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), FileBrowserAction.PREVIEW);
    }

    private void initialiseMouseListener() {
        list.addMouseListener(new MouseAdapter() {
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
    private final class OnGetChildrenObserver implements Observer<Path> {
        private final @NotNull Path directory;

        private final @NotNull List<FileNode> fileNodes = new ArrayList<>();

        OnGetChildrenObserver(@NotNull Path directory) {
            this.directory = directory;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void onComplete() {
            listModel.clear();

            fileNodes.forEach(listModel::addElement);

            FileBrowserComponent.this.directory = directory;
            notifyDirectoryChangeListeners();
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
            fileNodes.add(new FileNode(path));
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
}
