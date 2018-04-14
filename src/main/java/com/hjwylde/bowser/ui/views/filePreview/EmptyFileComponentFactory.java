package com.hjwylde.bowser.ui.views.filePreview;

import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.views.fileComponents.FileComponent;
import com.hjwylde.bowser.ui.views.fileComponents.FileComponentFactory;
import com.hjwylde.bowser.ui.views.fileComponents.TextFileComponentFactory;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.util.ResourceBundle;

/**
 * An empty {@link FileComponentFactory}. This factory generates a text component that informs the user that preview
 * functionality is unavailable. This is useful for files or directories that are unsupported, or when an exception
 * occurs in trying to display a specific file.
 */
@Immutable
public final class EmptyFileComponentFactory implements FileComponentFactory<FileComponent> {
    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(EmptyFileComponentFactory.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_EMPTY_LABEL = "emptyLabel";

    private static final @NotNull EmptyFileComponentFactory INSTANCE = new EmptyFileComponentFactory();

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
    public @NotNull FileComponent createFileComponent(@NotNull byte[] data) {
        TextFileComponentFactory fileComponentFactory = new TextFileComponentFactory();

        String content = RESOURCES.getString(RESOURCE_EMPTY_LABEL);
        FileComponent fileComponent = fileComponentFactory.createFileComponent(content.getBytes());

        return fileComponent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSupportedContentType(@NotNull String contentType) {
        return true;
    }
}
