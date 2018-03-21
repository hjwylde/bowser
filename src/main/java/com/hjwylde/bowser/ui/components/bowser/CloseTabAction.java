package com.hjwylde.bowser.ui.components.bowser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

final class CloseTabAction extends AbstractAction {
    private final static @NotNull Logger LOGGER = LogManager.getLogger(CloseTabAction.class.getSimpleName());

    private final @NotNull BowserView bowserView;

    CloseTabAction(@NotNull BowserView bowserView) {
        this.bowserView = Objects.requireNonNull(bowserView, "bowserView cannot be null.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        bowserView.removeCurrentFileBrowserTab();
    }
}
