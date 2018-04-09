package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.ui.dialogs.FilePreviewDialog;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Path;
import java.util.Optional;

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

        Path path = mPath.get();
        // TODO (hjw): This is an expensive operation, it should be backgrounded.
        previewFile(path, getFrame(view.getComponent()));
    }

    private @NotNull JFrame getFrame(@NotNull Component component) {
        return (JFrame) SwingUtilities.getWindowAncestor(component);
    }

    private void previewFile(@NotNull Path file, @NotNull JFrame parent) {
        FilePreviewDialog filePreviewDialog = FilePreviewDialog.builder()
                .parent(parent)
                .file(file)
                .build();

        filePreviewDialog.show();
    }
}
