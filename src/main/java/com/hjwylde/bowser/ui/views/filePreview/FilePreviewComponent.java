package com.hjwylde.bowser.ui.views.filePreview;

import com.hjwylde.bowser.ui.views.fileComponents.FileComponent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Objects;

final class FilePreviewComponent implements FilePreview.View {
    private final @NotNull FileComponent fileComponent;

    FilePreviewComponent(@NotNull FileComponent fileComponent) {
        this.fileComponent = Objects.requireNonNull(fileComponent, "fileComponent cannot be null");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JComponent getComponent() {
        return fileComponent.getComponent();
    }
}
