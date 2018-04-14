package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.ui.dialogs.FilePreviewDialog;
import com.hjwylde.bowser.util.concurrent.SwingExecutors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Immutable
final class PreviewAction extends AbstractAction {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(PreviewAction.class.getSimpleName());

    private final @NotNull FileBrowser.View view;

    PreviewAction(@NotNull FileBrowser.View view) {
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        Optional<Path> mPath = view.getSelectedPath();
        if (!mPath.isPresent()) {
            LOGGER.debug("PreviewAction called while no path is selected, doing nothing");
            return;
        }

        CompletableFuture.supplyAsync(() -> {
            Path path = mPath.get();

            return buildFilePreviewDialog(path, getFrame(view.getComponent()));
        }).thenAcceptAsync(FilePreviewDialog::show, SwingExecutors.edt());
    }

    private FilePreviewDialog buildFilePreviewDialog(@NotNull Path file, @NotNull JFrame parent) {
        return FilePreviewDialog.builder()
                .file(file)
                .parent(parent)
                .build();
    }

    private @NotNull JFrame getFrame(@NotNull Component component) {
        return (JFrame) SwingUtilities.getWindowAncestor(component);
    }
}
