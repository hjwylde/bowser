package com.hjwylde.bowser;

import com.hjwylde.bowser.io.file.DefaultFileSystemFactory;
import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.Bowser;
import com.hjwylde.bowser.ui.BowserFrame;
import com.hjwylde.bowser.ui.views.tabbedFileBrowser.TabbedFileBrowser;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
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
            LOGGER.warn(e.getMessage(), e);
        }

        SwingUtilities.invokeLater(Application::build);
    }

    private static @NotNull BowserFrame build() {
        return Bowser.builder()
                .title(getTitle())
                .tabbedFileBrowserView(buildTabbedFileBrowserView())
                .build();
    }

    private static @NotNull TabbedFileBrowser.View buildTabbedFileBrowserView() {
        TabbedFileBrowser.View tabbedFileBrowserView = TabbedFileBrowser.builder()
                .fileSystemFactory(DefaultFileSystemFactory.getInstance())
                .build();

        tabbedFileBrowserView.addTab();

        return tabbedFileBrowserView;
    }

    private static @NotNull String getTitle() {
        return RESOURCES.getString(RESOURCE_TITLE);
    }
}
