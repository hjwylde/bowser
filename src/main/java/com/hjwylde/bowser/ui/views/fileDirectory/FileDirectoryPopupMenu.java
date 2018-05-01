package com.hjwylde.bowser.ui.views.fileDirectory;

import com.hjwylde.bowser.modules.LocaleModule;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ResourceBundle;

final class FileDirectoryPopupMenu extends JPopupMenu {
    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(FileDirectoryPopupMenu.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_OPEN = "open";
    private static final @NotNull String RESOURCE_COPY_PATH = "copyPath";

    private final @NotNull FileDirectory.View view;

    FileDirectoryPopupMenu(@NotNull FileDirectory.View view) {
        this.view = view;

        initialisePopupMenu();
    }

    private void initialisePopupMenu() {
        JMenuItem openMenuItem = new JMenuItem(RESOURCES.getString(RESOURCE_OPEN));
        openMenuItem.addActionListener(e -> new OpenAction(view).run());
        add(openMenuItem);

        addSeparator();

        JMenuItem copyPathMenuItem = new JMenuItem(RESOURCES.getString(RESOURCE_COPY_PATH));
        copyPathMenuItem.addActionListener(e -> new CopyPathAction(view).run());
        add(copyPathMenuItem);
    }
}
