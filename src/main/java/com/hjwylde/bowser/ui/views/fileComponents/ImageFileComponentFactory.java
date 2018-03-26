package com.hjwylde.bowser.ui.views.fileComponents;

import com.google.auto.service.AutoService;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.Arrays;
import java.util.List;

/**
 * A {@link FileComponentFactory} that supports creating image {@link FileComponent}s ("image/jpeg", "image/png" files).
 */
@AutoService(FileComponentFactory.class)
public final class ImageFileComponentFactory implements FileComponentFactory<ImageFileComponent> {
    private static final List<String> SUPPORTED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/png");

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ImageFileComponent createFileComponent(@NotNull byte[] data) {
        ImageIcon image = new ImageIcon(data);

        return new ImageFileComponent(image);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSupportedContentType(@NotNull String contentType) {
        return SUPPORTED_CONTENT_TYPES.contains(contentType);
    }
}
