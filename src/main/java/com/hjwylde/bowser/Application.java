package com.hjwylde.bowser;

import com.hjwylde.bowser.io.DefaultFileSystemFactory;
import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.components.View;
import com.hjwylde.bowser.ui.components.bowser.BowserBuilder;
import com.hjwylde.bowser.ui.components.bowser.BowserView;
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
            LOGGER.warn(e);
        }

        SwingUtilities.invokeLater(Application::buildBowserView);
    }

    private static @NotNull View buildBowserView() {
        BowserView bowserView = new BowserBuilder()
                .title(getTitle())
                .fileSystemFactory(DefaultFileSystemFactory.getInstance())
                .build();

        bowserView.addDefaultFileBrowserTab();

        return bowserView;
    }

    private static @NotNull String getTitle() {
        return RESOURCES.getString(RESOURCE_TITLE);
    }
}
