package com.hjwylde.bowser.ui.views.fileComponents;

import com.google.auto.service.AutoService;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

/**
 * A {@link FileComponentFactory} that supports creating text {@link FileComponent}s ("text/plain" files).
 */
@AutoService(FileComponentFactory.class)
@SuppressWarnings("unused")
public final class TextFileComponentFactory implements FileComponentFactory<TextFileComponent> {
    private static final List<String> SUPPORTED_CONTENT_TYPES = Collections.singletonList("text/plain");

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull TextFileComponent createFileComponent(@NotNull byte[] data) {
        String text = new String(data);

        return new TextFileComponent(text);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSupportedContentType(@NotNull String contentType) {
        return SUPPORTED_CONTENT_TYPES.contains(contentType);
    }
}
