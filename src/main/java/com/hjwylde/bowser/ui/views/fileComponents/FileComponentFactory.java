package com.hjwylde.bowser.ui.views.fileComponents;

import org.jetbrains.annotations.NotNull;

/**
 * A factory for creating arbitrary file components for different content types. For example, subclasses may create
 * components for text or image data.
 *
 * @param <E> the file component type that this factory creates.
 */
public interface FileComponentFactory<E extends FileComponent> {
    /**
     * Creates a new file component with the given data.
     *
     * @param data the data for the component
     * @return a new file component.
     */
    @NotNull E createFileComponent(@NotNull byte[] data);

    /**
     * Checks to see whether the given content type is supported by this factory. If this method returns true, it is
     * assumed that {@link #createFileComponent(byte[])} will be able to successfully create a {@link FileComponent}.
     *
     * @param contentType the content type to check.
     * @return true if the content type is supported.
     */
    boolean isSupportedContentType(@NotNull String contentType);
}
