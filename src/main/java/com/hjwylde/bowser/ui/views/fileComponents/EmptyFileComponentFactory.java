package com.hjwylde.bowser.ui.views.fileComponents;

import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;

/**
 * An empty {@link FileComponentFactory}. This factory generates a text component that informs the user that preview
 * functionality is unavailable. This is useful for files or directories that are unsupported, or when an exception
 * occurs in trying to display a specific file.
 */
@Immutable
public final class EmptyFileComponentFactory implements FileComponentFactory<EmptyFileComponent> {
    private static final @NotNull EmptyFileComponentFactory INSTANCE = new EmptyFileComponentFactory();

    private static final @NotNull EmptyFileComponent EMPTY_FILE_COMPONENT = new EmptyFileComponent();

    private EmptyFileComponentFactory() {
    }

    /**
     * Gets the singleton {@link EmptyFileComponentFactory} instance.
     *
     * @return the singleton instance.
     */
    public static @NotNull FileComponentFactory<? extends FileComponent> getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * <p>
     * N.B., in this subclass {@code data} is unused and it is acceptable to pass an empty {@code byte} array.
     */
    @Override
    public @NotNull EmptyFileComponent createFileComponent(@NotNull byte[] data) {
        return EMPTY_FILE_COMPONENT;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSupportedContentType(@NotNull String contentType) {
        return true;
    }
}
