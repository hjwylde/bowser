package com.hjwylde.bowser.ui.views.fileComponents;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.awt.*;

@NotThreadSafe
final class TextFileComponent implements FileComponent {
    private final @NotNull JComponent root = new JPanel(new FlowLayout(FlowLayout.LEFT));
    private final @NotNull JTextPane textPane = new JTextPane();

    TextFileComponent(@NotNull String text) {
        textPane.setText(text);
        textPane.setEditable(false);

        root.add(textPane);
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
