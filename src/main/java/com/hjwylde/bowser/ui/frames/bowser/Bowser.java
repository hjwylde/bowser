package com.hjwylde.bowser.ui.frames.bowser;

import com.hjwylde.bowser.io.file.DefaultFileSystemFactory;
import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.views.tabbedFileBrowser.TabbedFileBrowser;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.ResourceBundle;

/**
 * An uninstantiable namespace.
 */
public final class Bowser {
    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(Bowser.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_FILE = "file";
    private static final @NotNull String RESOURCE_CLOSE_TAB = "closeTab";
    private static final @NotNull String RESOURCE_NEW_FTP_TAB = "newFtpTab";
    private static final @NotNull String RESOURCE_NEW_TAB = "newTab";
    private static final @NotNull String RESOURCE_OPEN = "open";
    private static final @NotNull String RESOURCE_VIEW = "view";
    private static final @NotNull String RESOURCE_SORT_BY = "sortBy";
    private static final @NotNull String RESOURCE_SORT_BY_NAME = "sortByName";
    private static final @NotNull String RESOURCE_SORT_BY_SIZE = "sortBySize";

    private Bowser() {
    }

    /**
     * Creates a new {@link Builder} and returns it. The builder is used to set up and show a {@link BowserFrame} to the
     * user.
     *
     * @return a new {@link Builder}.
     */
    public static @NotNull Builder builder() {
        return new Builder();
    }

    @NotThreadSafe
    public static final class Builder {
        private static final @NotNull Comparator<Path> NAME_COMPARATOR = Comparator.comparing(Path::getFileName);
        private static final @NotNull Comparator<Path> SIZE_COMPARATOR = Comparator.comparing(path -> {
            if (Files.isDirectory(path)) {
                return -1L;
            }

            try {
                return Files.size(path);
            } catch (IOException ignored) {
                // 0 is a valid size, so return -1
                return -1L;
            }
        });

        private final @NotNull JFrame frame = new JFrame();

        private boolean built = false;

        private Builder() {
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        }

        /**
         * Builds a {@link BowserFrame} and shows it to the user. The frame is setup with a menu bar and the provided
         * {@link TabbedFileBrowser.View}. This method may only be called once. Any repeated calls will result in a
         * {@link IllegalStateException}.
         *
         * @return the built and shown {@link BowserFrame}.
         * @throws IllegalStateException if called repeatedly.
         */
        public @NotNull BowserFrame build() {
            if (built) {
                throw new IllegalStateException("A BowserFrame may only be built once.");
            }

            built = true;

            TabbedFileBrowser.View tabbedFileBrowserView = buildTabbedFileBrowserView();
            frame.add(tabbedFileBrowserView.getComponent());

            JMenuBar menuBar = buildMenuBar(tabbedFileBrowserView);
            frame.setJMenuBar(menuBar);

            frame.pack();
            frame.setVisible(true);

            return new BowserFrame(frame, tabbedFileBrowserView);
        }

        public @NotNull Builder title(@NotNull String title) {
            frame.setTitle(title);

            return this;
        }

        private @NotNull JMenuBar buildMenuBar(@NotNull TabbedFileBrowser.View tabbedFileBrowserView) {
            JMenuBar menuBar = new JMenuBar();

            JMenu fileMenu = new JMenu(RESOURCES.getString(RESOURCE_FILE));
            menuBar.add(fileMenu);

            JMenuItem newTabMenuItem = new JMenuItem(RESOURCES.getString(RESOURCE_NEW_TAB), KeyEvent.VK_T);
            newTabMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            newTabMenuItem.addActionListener(e -> tabbedFileBrowserView.addTab());
            fileMenu.add(newTabMenuItem);

            JMenuItem newFtpTabMenuItem = new JMenuItem(RESOURCES.getString(RESOURCE_NEW_FTP_TAB), KeyEvent.VK_Y);
            newFtpTabMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            newFtpTabMenuItem.addActionListener(e -> new NewFtpTabAction(tabbedFileBrowserView).run());
            fileMenu.add(newFtpTabMenuItem);

            JMenuItem openMenuItem = new JMenuItem(RESOURCES.getString(RESOURCE_OPEN), KeyEvent.VK_O);
            openMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            openMenuItem.addActionListener(e -> new OpenAction(tabbedFileBrowserView).run());
            fileMenu.add(openMenuItem);

            fileMenu.addSeparator();

            JMenuItem closeTabMenuItem = new JMenuItem(RESOURCES.getString(RESOURCE_CLOSE_TAB), KeyEvent.VK_W);
            closeTabMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            closeTabMenuItem.addActionListener(e -> tabbedFileBrowserView.removeCurrentTab());
            fileMenu.add(closeTabMenuItem);

            JMenu viewMenu = new JMenu(RESOURCES.getString(RESOURCE_VIEW));
            menuBar.add(viewMenu);

            JMenuItem sortByMenu = new JMenu(RESOURCES.getString(RESOURCE_SORT_BY));
            viewMenu.add(sortByMenu);

            JMenuItem sortByNameMenuItem = new JMenuItem(RESOURCES.getString(RESOURCE_SORT_BY_NAME));
            sortByNameMenuItem.addActionListener(e -> new SortByAction(tabbedFileBrowserView, NAME_COMPARATOR).run());
            sortByMenu.add(sortByNameMenuItem);

            JMenuItem sortBySizeMenuItem = new JMenuItem(RESOURCES.getString(RESOURCE_SORT_BY_SIZE));
            sortBySizeMenuItem.addActionListener(e -> new SortByAction(tabbedFileBrowserView, SIZE_COMPARATOR).run());
            sortByMenu.add(sortBySizeMenuItem);

            return menuBar;
        }

        private @NotNull TabbedFileBrowser.View buildTabbedFileBrowserView() {
            TabbedFileBrowser.View tabbedFileBrowserView = TabbedFileBrowser.builder()
                    .fileSystemFactory(DefaultFileSystemFactory.getInstance())
                    .build();

            tabbedFileBrowserView.addTab();

            return tabbedFileBrowserView;
        }
    }
}
