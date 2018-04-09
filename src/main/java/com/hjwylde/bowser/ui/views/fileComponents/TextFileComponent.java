package com.hjwylde.bowser.ui.views.fileComponents;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

class TextFileComponent implements FileComponent {
    private final @NotNull JTextPane textPane = new JTextPane();

    TextFileComponent(@NotNull String text) {
        textPane.setText(text);
        textPane.setEditable(false);
        textPane.setBorder(new EmptyBorder(10, 4, 10, 10));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JComponent getComponent() {
        return textPane;
    }
}