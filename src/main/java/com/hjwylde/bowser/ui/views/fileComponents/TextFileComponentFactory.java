package com.hjwylde.bowser.ui.views.fileComponents;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;

/**
 * A {@link FileComponentFactory} that supports creating text {@link FileComponent}s ("text/plain" files).
 */
@Immutable
@SuppressWarnings("unused")
public final class TextFileComponentFactory implements FileComponentFactory<TextFileComponent> {
    private static final String SUPPORTED_CONTENT_TYPE = "text/";

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
        return contentType.startsWith(SUPPORTED_CONTENT_TYPE);
    }
}
