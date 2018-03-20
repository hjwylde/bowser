package com.hjwylde.bowser;

import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.components.View;
import com.hjwylde.bowser.ui.components.bowser.BowserBuilder;
import com.hjwylde.bowser.ui.components.fileBrowser.FileBrowserBuilder;
import com.hjwylde.bowser.ui.components.fileBrowser.FileBrowserView;
import com.hjwylde.bowser.ui.components.scrollable.ScrollableView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.nio.file.FileSystems;
import java.util.ResourceBundle;

public final class Application {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(Application.class.getSimpleName());

    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(Application.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_TITLE = "title";

    private Application() {
    }

    public static void main(String[] args) {
        try {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
        } catch (SecurityException e) {
            LOGGER.warn(e);
        }

        SwingUtilities.invokeLater(Application::buildBowserView);
    }

    private static @NotNull View buildBowserView() {
        return new BowserBuilder()
                .title(getTitle())
                .addView(buildFileBrowserView())
                .build();
    }

    private static @NotNull View buildFileBrowserView() {
        FileBrowserView fileBrowserView = new FileBrowserBuilder()
                .fileSystem(FileSystems.getDefault())
                .build();

        return new ScrollableView(fileBrowserView);
    }

    private static @NotNull String getTitle() {
        return RESOURCES.getString(RESOURCE_TITLE);
    }
}
