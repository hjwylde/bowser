package com.hjwylde.bowser.ui.views.filePreview;

import com.hjwylde.bowser.ui.views.fileComponents.FileComponent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

final class EmptyFileComponent implements FileComponent {
    private final @NotNull JComponent component = new JPanel();

    EmptyFileComponent() {
        component.setBackground(Color.WHITE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JComponent getComponent() {
        return component;
    }
}
