package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.modules.LocaleModule;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.Stream;

@NotThreadSafe
public final class FileBrowserComponent implements FileBrowser.View {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(FileBrowserComponent.class.getSimpleName());

    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(FileBrowserComponent.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_ERROR_BROWSING_PATH = "errorBrowsingPath";

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
        OnGetChildrenObserver handler = new OnGetChildrenObserver(directory);

        viewModel.getChildren(directory)
                .handleAsync(handler, SwingExecutors.edt());
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
    private final class OnGetChildrenObserver implements BiFunction<Stream<Path>, Throwable, Void> {
        private final @NotNull Path directory;

        OnGetChildrenObserver(@NotNull Path directory) {
            this.directory = directory;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public Void apply(Stream<Path> pathStream, Throwable throwable) {
            if (pathStream != null) {
                onSuccess(pathStream);
            } else if (throwable != null) {
                onError(throwable);
            }

            return null;
        }

        private void onError(@NotNull Throwable throwable) {
            Exception e = new Exception(RESOURCES.getString(RESOURCE_ERROR_BROWSING_PATH), throwable);
            LOGGER.warn(e.getMessage(), e);

            handleError(e);
        }

        private void onSuccess(@NotNull Stream<Path> pathStream) {
            listModel.clear();

            pathStream.map(FileNode::new)
                    .forEach(listModel::addElement);

            FileBrowserComponent.this.directory = directory;
            notifyDirectoryChangeListeners();
        }
    }
}
