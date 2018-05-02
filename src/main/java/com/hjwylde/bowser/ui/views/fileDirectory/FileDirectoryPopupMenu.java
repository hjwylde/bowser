package com.hjwylde.bowser.ui.views.fileDirectory;

import com.hjwylde.bowser.modules.LocaleModule;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.ResourceBundle;

final class FileDirectoryPopupMenu extends JPopupMenu {
    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(FileDirectoryPopupMenu.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_OPEN = "open";
    private static final @NotNull String RESOURCE_COPY_PATH = "copyPath";
    private static final @NotNull String RESOURCE_SORT_BY = "sortBy";
    private static final @NotNull String RESOURCE_SORT_BY_NAME = "sortByName";
    private static final @NotNull String RESOURCE_SORT_BY_SIZE = "sortBySize";

    private final @NotNull FileDirectory.View view;

    FileDirectoryPopupMenu(@NotNull FileDirectory.View view) {
        this.view = view;

        initialisePopupMenu();
    }

    private void initialisePopupMenu() {
        JMenuItem openMenuItem = new JMenuItem(RESOURCES.getString(RESOURCE_OPEN));
        openMenuItem.addActionListener(e -> new OpenAction(view).run());
        add(openMenuItem);

        JMenuItem copyPathMenuItem = new JMenuItem(RESOURCES.getString(RESOURCE_COPY_PATH));
        copyPathMenuItem.addActionListener(e -> new CopyPathAction(view).run());
        add(copyPathMenuItem);

        addSeparator();

        JMenu sortByMenu = new JMenu(RESOURCES.getString(RESOURCE_SORT_BY));
        add(sortByMenu);

        JMenuItem sortByNameMenuItem = new JMenuItem(RESOURCES.getString(RESOURCE_SORT_BY_NAME));
        sortByNameMenuItem.addActionListener(e -> view.sort(FileNode.NAME_COMPARATOR));
        sortByMenu.add(sortByNameMenuItem);

        JMenuItem sortBySizeMenuItem = new JMenuItem(RESOURCES.getString(RESOURCE_SORT_BY_SIZE));
        sortBySizeMenuItem.addActionListener(e -> view.sort(FileNode.SIZE_COMPARATOR));
        sortByMenu.add(sortBySizeMenuItem);
    }
}
