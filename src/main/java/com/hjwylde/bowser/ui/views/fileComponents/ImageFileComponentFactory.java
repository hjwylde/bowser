package com.hjwylde.bowser.ui.views.fileComponents;

import com.hjwylde.bowser.io.RestrictedInputStream;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * A {@link FileComponentFactory} that supports creating image {@link FileComponent}s ("image/jpeg", "image/png" files).
 */
@Immutable
@SuppressWarnings("unused")
public final class ImageFileComponentFactory implements FileComponentFactory<ImageFileComponent> {
    private static final int MAX_BYTES = 1024 * 1024 * 1024; // 1 GB

    private static final List<String> SUPPORTED_CONTENT_TYPES = Arrays.asList("image/jpeg", "image/png");

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ImageFileComponent createFileComponent(@NotNull InputStream in) throws IOException {
        RestrictedInputStream rin = new RestrictedInputStream(in, MAX_BYTES);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            for (int count = rin.read(buffer); count >= 0; count = rin.read(buffer)) {
                out.write(buffer, 0, count);
            }

            byte[] data = out.toByteArray();

            return createFileComponent(data);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSupportedContentType(@NotNull String contentType) {
        return SUPPORTED_CONTENT_TYPES.contains(contentType);
    }

    private @NotNull ImageFileComponent createFileComponent(@NotNull byte[] data) {
        ImageIcon image = new ImageIcon(data);

        return new ImageFileComponent(image);
    }
}
