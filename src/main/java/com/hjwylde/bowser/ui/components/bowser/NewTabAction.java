package com.hjwylde.bowser.ui.components.bowser;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

final class NewTabAction extends AbstractAction {
    private final @NotNull BowserView bowserView;

    NewTabAction(@NotNull BowserView bowserView) {
        this.bowserView = Objects.requireNonNull(bowserView, "bowserView cannot be null.");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        bowserView.addDefaultFileBrowserTab();
    }
}
