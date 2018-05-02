package com.hjwylde.bowser.ui.frames.bowser;

import com.hjwylde.bowser.modules.ExecutorServiceModule;
import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.views.tabbedFileBrowser.TabbedFileBrowser;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.Immutable;
import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
        initialiseDismissListener();
    }

    private void initialiseDismissListener() {
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                ExecutorServiceModule.provideExecutorService().shutdown();
            }
        });
    }

    private void initialiseTabChangeListener() {
        tabbedFileBrowserView.addTabChangeListener(mView -> {
            StringBuilder sb = new StringBuilder(RESOURCES.getString(RESOURCE_TITLE));

            mView.ifPresent(view -> sb.append(" - ").append(view.getTitle()));

            frame.setTitle(sb.toString());
        });
    }
}
