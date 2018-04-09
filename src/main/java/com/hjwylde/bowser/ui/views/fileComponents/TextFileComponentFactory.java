package com.hjwylde.bowser.ui.views.fileComponents;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.util.Collections;
import java.util.List;

/**
 * A {@link FileComponentFactory} that supports creating text {@link FileComponent}s ("text/plain" files).
 */
@Immutable
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
