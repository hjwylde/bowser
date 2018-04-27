package com.hjwylde.bowser.ui.frames.bowser;

import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.views.tabbedFileBrowser.TabbedFileBrowser;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import javax.swing.*;
import java.util.ResourceBundle;

/**
 * An empty shell that holds onto the application {@link JFrame}. While it's unused right now, future features may need
 * it.
 */
@Immutable
public final class BowserFrame {
    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(BowserFrame.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_TITLE = "title";

    private final @NotNull JFrame frame;

    private final @NotNull TabbedFileBrowser.View tabbedFileBrowserView;

    BowserFrame(@NotNull JFrame frame, @NotNull TabbedFileBrowser.View tabbedFileBrowserView) {
        this.frame = frame;

        this.tabbedFileBrowserView = tabbedFileBrowserView;

        initialiseTabChangeListener();
    }

    private void initialiseTabChangeListener() {
        tabbedFileBrowserView.addTabChangeListener(view -> {
            String title = RESOURCES.getString(RESOURCE_TITLE) + " - " + view.getTitle();

            frame.setTitle(title);
        });
    }
}
