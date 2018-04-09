package com.hjwylde.bowser.ui.frames.bowser;

import com.hjwylde.bowser.modules.LocaleModule;
import com.hjwylde.bowser.ui.views.tabbedFileBrowser.TabbedFileBrowser;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.ResourceBundle;

/**
 * An uninstantiable namespace.
 */
public final class Bowser {
    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(Bowser.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_CLOSE_TAB = "closeTab";
    private static final @NotNull String RESOURCE_FILE = "file";
    private static final @NotNull String RESOURCE_NEW_FTP_TAB = "newFtpTab";
    private static final @NotNull String RESOURCE_NEW_TAB = "newTab";

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
        private final @NotNull JFrame frame = new JFrame();

        private TabbedFileBrowser.View tabbedFileBrowserView;

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
         * @throws IllegalStateException if tabbedFileBrowserView is null.
         */
        public @NotNull BowserFrame build() {
            if (built) {
                throw new IllegalStateException("A BowserFrame may only be built once.");
            }

            built = true;

            if (tabbedFileBrowserView == null) {
                throw new IllegalStateException("tabbedFileBrowser cannot be null.");
            }

            frame.add(tabbedFileBrowserView.getComponent());
            frame.setJMenuBar(buildMenuBar());

            frame.pack();
            frame.setVisible(true);

            return new BowserFrame(frame);
        }

        public @NotNull Builder tabbedFileBrowserView(@NotNull TabbedFileBrowser.View tabbedFileBrowserView) {
            this.tabbedFileBrowserView = tabbedFileBrowserView;

            return this;
        }

        public @NotNull Builder title(@NotNull String title) {
            frame.setTitle(title);

            return this;
        }

        private @NotNull JMenuBar buildMenuBar() {
            JMenuBar menuBar = new JMenuBar();

            JMenu fileMenu = new JMenu(RESOURCES.getString(RESOURCE_FILE));
            menuBar.add(fileMenu);

            JMenuItem newTabMenuItem = new JMenuItem(RESOURCES.getString(RESOURCE_NEW_TAB), KeyEvent.VK_T);
            newTabMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            // TODO (hjw): This is potentially an expensive operation, it should be backgrounded.
            newTabMenuItem.addActionListener(e -> tabbedFileBrowserView.addTab());
            fileMenu.add(newTabMenuItem);

            JMenuItem newFtpTabMenuItem = new JMenuItem(RESOURCES.getString(RESOURCE_NEW_FTP_TAB), KeyEvent.VK_Y);
            newFtpTabMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            // TODO (hjw): This is potentially an expensive operation, it should be backgrounded.
            newFtpTabMenuItem.addActionListener(e -> tabbedFileBrowserView.addFtpTab());
            fileMenu.add(newFtpTabMenuItem);

            fileMenu.addSeparator();

            JMenuItem closeTabMenuItem = new JMenuItem(RESOURCES.getString(RESOURCE_CLOSE_TAB), KeyEvent.VK_W);
            closeTabMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            closeTabMenuItem.addActionListener(e -> tabbedFileBrowserView.removeCurrentTab());
            fileMenu.add(closeTabMenuItem);

            return menuBar;
        }
    }
}
