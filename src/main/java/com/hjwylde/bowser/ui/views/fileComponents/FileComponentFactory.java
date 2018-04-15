package com.hjwylde.bowser.ui.views.fileComponents;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.InputStream;

/**
 * A factory for creating arbitrary file components for different content types. For example, subclasses may create
 * components for text or image data.
 *
 * @param <E> the file component type that this factory creates.
 */
public interface FileComponentFactory<E extends FileComponent> {
    /**
     * Creates a new file component from the given {@link InputStream}.
     *
     * @param in the input stream for the component.
     * @return a new file component.
     * @throws IOException if unable to read the input stream, or the input stream is too large.
     */
    @NotNull E createFileComponent(@NotNull InputStream in) throws IOException;

    /**
     * Checks to see whether the given content type is supported by this factory. If this method returns true, it is
     * assumed that {@link #createFileComponent(InputStream)} will be able to successfully create a
     * {@link FileComponent}.
     *
     * @param contentType the content type to check.
     * @return true if the content type is supported.
     */
    boolean isSupportedContentType(@NotNull String contentType);
}
