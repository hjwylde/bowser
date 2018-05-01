package com.hjwylde.bowser.ui.views.filePreview;

import com.hjwylde.bowser.ui.views.fileComponents.FileComponent;
import com.hjwylde.bowser.ui.views.fileComponents.FileComponentFactory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.io.InputStream;

/**
 * An empty {@link FileComponentFactory}. This factory generates a text component that informs the user that preview
 * functionality is unavailable. This is useful for files or directories that are unsupported, or when an exception
 * occurs in trying to display a specific file.
 */
@Immutable
final class EmptyFileComponentFactory implements FileComponentFactory<FileComponent> {
    private static final @NotNull EmptyFileComponentFactory INSTANCE = new EmptyFileComponentFactory();

    private EmptyFileComponentFactory() {
    }

    /**
     * Gets the singleton {@link EmptyFileComponentFactory} instance.
     *
     * @return the singleton instance.
     */
    static @NotNull EmptyFileComponentFactory getInstance() {
        return INSTANCE;
    }

    /**
     * {@inheritDoc}
     * <p>
     * N.B., in this subclass {@code in} is unused and it is acceptable to pass an empty input stream.
     */
    @Override
    public @NotNull FileComponent createFileComponent(@NotNull InputStream ignored) {
        return createFileComponent();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSupportedContentType(@NotNull String contentType) {
        return true;
    }

    @NotNull FileComponent createFileComponent() {
        return new EmptyFileComponent();
    }
}
