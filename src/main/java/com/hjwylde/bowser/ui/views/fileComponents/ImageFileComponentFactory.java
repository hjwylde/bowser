package com.hjwylde.bowser.ui.views.fileComponents;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import javax.swing.*;
import java.util.Arrays;
import java.util.List;

/**
 * A {@link FileComponentFactory} that supports creating image {@link FileComponent}s ("image/jpeg", "image/png" files).
 */
@Immutable
@SuppressWarnings("unused")
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
