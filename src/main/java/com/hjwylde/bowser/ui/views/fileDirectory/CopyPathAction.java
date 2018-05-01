package com.hjwylde.bowser.ui.views.fileDirectory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.nio.file.Path;
import java.util.Optional;

@NotThreadSafe
final class CopyPathAction implements Runnable {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(CopyPathAction.class.getSimpleName());

    private final @NotNull FileDirectory.View view;

    CopyPathAction(@NotNull FileDirectory.View view) {
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        Optional<Path> mPath = view.getSelectedPath();
        if (!mPath.isPresent()) {
            LOGGER.debug("CopyPathAction called while no path is selected, doing nothing");
            return;
        }

        String path = mPath.get().toUri().toString();

        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(new StringSelection(path.toString()), null);
    }
}
