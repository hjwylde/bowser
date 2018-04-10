package com.hjwylde.bowser.ui.views.fileBrowser;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.awt.event.KeyEvent;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@NotThreadSafe
public final class FileBrowserComponent implements FileBrowser.View {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(FileBrowserComponent.class.getSimpleName());

    private final @NotNull DefaultListModel<FileNode> listModel = new DefaultListModel<>();
    private final @NotNull JList<FileNode> list = new JList<>(listModel);

    private final @NotNull FileBrowserViewModel viewModel;

    FileBrowserComponent(@NotNull Path startingPath, @NotNull FileBrowserViewModel viewModel) {
        this.viewModel = viewModel;

        initialiseInputMap();
        initialiseActionMap();
        initialiseList(startingPath);
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

    private void initialiseActionMap() {
        // TODO (hjw): Cyclic references -> is it possible to avoid this?
        list.getActionMap().put(FileBrowserAction.OPEN, new OpenAction(this));
        list.getActionMap().put(FileBrowserAction.PREVIEW, new PreviewAction(this));
    }

    private void initialiseInputMap() {
        list.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), FileBrowserAction.OPEN);
        list.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), FileBrowserAction.PREVIEW);
    }

    private void initialiseList(@NotNull Path startingPath) {
        viewModel.getChildren(startingPath)
                .subscribe(new OnGetChildrenObserver());
    }

    @NotThreadSafe
    private final class OnGetChildrenObserver implements Observer<Path> {
        private final @NotNull List<FileNode> fileNodes = new ArrayList<>();

        /**
         * {@inheritDoc}
         */
        @Override
        public void onComplete() {
            listModel.clear();

            fileNodes.forEach(listModel::addElement);
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
