package com.hjwylde.bowser.ui.views.fileComponents;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.awt.*;

@NotThreadSafe
final class ImageFileComponent implements FileComponent {
    private final @NotNull JComponent root = new JPanel(new FlowLayout(FlowLayout.CENTER));
    private final @NotNull JLabel label;

    ImageFileComponent(@NotNull ImageIcon image) {
        label = new JLabel(image);

        root.add(label);
        root.setOpaque(false);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JComponent getComponent() {
        return root;
    }
}
