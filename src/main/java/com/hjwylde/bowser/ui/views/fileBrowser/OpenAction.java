package com.hjwylde.bowser.ui.views.fileBrowser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.nio.file.Path;

final class OpenAction extends AbstractAction {
    private final static @NotNull Logger LOGGER = LogManager.getLogger(OpenAction.class.getSimpleName());

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!(e.getSource() instanceof JTree)) {
            throw new IllegalArgumentException("event source must be an instance of JTree, instead it was " + e.getSource().getClass());
        }

        JTree tree = (JTree) e.getSource();
        FileTreeNode node = (FileTreeNode) tree.getLastSelectedPathComponent();
        if (node.isDirectory()) {
            return;
        }

        Path file = node.getFilePath();
        openFile(file);
    }

    private void openFile(@NotNull Path file) {
        if (!Desktop.isDesktopSupported()) {
            LOGGER.warn("Desktop is not supported.");
            return;
        }

        try {
            Desktop.getDesktop().open(file.toFile());
        } catch (IOException e) {
            LOGGER.warn(e.getMessage(), e);
        }
    }
}
