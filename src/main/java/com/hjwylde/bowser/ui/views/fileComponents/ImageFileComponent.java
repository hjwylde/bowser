package com.hjwylde.bowser.ui.views.fileComponents;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;

@NotThreadSafe
final class ImageFileComponent implements FileComponent {
    private final @NotNull JLabel label;

    ImageFileComponent(@NotNull ImageIcon image) {
        label = new JLabel(image);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JComponent getComponent() {
        return label;
    }
}
