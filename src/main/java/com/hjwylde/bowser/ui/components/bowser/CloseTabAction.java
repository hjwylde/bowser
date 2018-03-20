package com.hjwylde.bowser.ui.components.bowser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;

final class CloseTabAction extends AbstractAction {
    private final static @NotNull Logger LOGGER = LogManager.getLogger(CloseTabAction.class.getSimpleName());

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!(e.getSource() instanceof JTabbedPane)) {
            LOGGER.error("event source must be an instance of JTabbedPane.");
            return;
        }

        JTabbedPane tabbedPane = (JTabbedPane) e.getSource();
        tabbedPane.remove(tabbedPane.getSelectedComponent());
    }
}
