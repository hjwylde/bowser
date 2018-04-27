package com.hjwylde.bowser.ui.views.fileDirectory;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.nio.file.Path;

@NotThreadSafe
final class PreviousAction extends AbstractAction {
    private final @NotNull FileDirectory.View view;

    PreviousAction(@NotNull FileDirectory.View view) {
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        Path directory = view.getDirectory();
        Path parent = directory.getParent();

        view.setDirectory(parent);
    }
}
