package com.hjwylde.bowser.ui.views.filePreview;

import com.hjwylde.bowser.ui.views.fileComponents.FileComponent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

final class FilePreviewComponent implements FilePreview.View {
    private final @NotNull FileComponent fileComponent;

    FilePreviewComponent(@NotNull FileComponent fileComponent) {
        this.fileComponent = fileComponent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JComponent getComponent() {
        return fileComponent.getComponent();
    }
}
