package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.modules.LocaleModule;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

@Immutable
final class OpenWithAssociatedApplicationStrategy implements OpenStrategy {
    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(OpenWithAssociatedApplicationStrategy.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_ERROR_DESKTOP_NOT_SUPPORTED = "errorDesktopNotSupported";

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSupported(@NotNull Path path) {
        // Let the file system try determine whether there is a default associated application for this file type.
        return !Files.isDirectory(path);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void open(@NotNull Path path) throws IOException {
        if (!Desktop.isDesktopSupported()) {
            throw new IOException(RESOURCES.getString(RESOURCE_ERROR_DESKTOP_NOT_SUPPORTED));
        }

        try {
            // May throw UnsupportedOperationException depending on the underlying FileSystem
            File file = path.toFile();

            Desktop.getDesktop().open(file);
        } catch (IOException | UnsupportedOperationException e) {
            throw new IOException(RESOURCES.getString(RESOURCE_ERROR_DESKTOP_NOT_SUPPORTED), e);
        }
    }
}
