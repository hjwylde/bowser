package com.hjwylde.bowser.ui.components.bowser;

import com.hjwylde.bowser.io.FileSystemFactory;
import com.hjwylde.bowser.modules.LocaleModule;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Objects;
import java.util.ResourceBundle;

public final class BowserBuilder {
    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(BowserBuilder.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_CLOSE_TAB = "closeTab";
    private static final @NotNull String RESOURCE_FILE = "file";
    private static final @NotNull String RESOURCE_NEW_TAB = "newTab";

    private final @NotNull JFrame frame = new JFrame();
    private final @NotNull JTabbedPane tabbedPane = new JTabbedPane();

    private FileSystemFactory fileSystemFactory;

    private boolean built = false;

    public BowserBuilder() {
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.add(tabbedPane);

        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
    }

    public @NotNull BowserView build() {
        // I'm not sure of the best way to do this. I could look at frame#isVisible, or perhaps there are other
        // methods as well. Given I don't know the corner cases here, using a variable seems the safest option.
        if (built) {
            throw new IllegalStateException("A BowserView may only be built once.");
        }

        built = true;

        frame.pack();
        frame.setVisible(true);

        // This is done temporarily just to test the menu system. This is not really ideal due to the cycles introduced
        // here
        BowserView bowserView = new BowserView(frame, tabbedPane, fileSystemFactory);

        frame.setJMenuBar(buildMenuBar(bowserView));

        return bowserView;
    }

    public @NotNull BowserBuilder fileSystemFactory(@NotNull FileSystemFactory fileSystemFactory) {
        this.fileSystemFactory = Objects.requireNonNull(fileSystemFactory, "fileSystemFactory cannot be null.");

        return this;
    }

    public @NotNull BowserBuilder title(@NotNull String title) {
        frame.setTitle(title);

        return this;
    }

    private @NotNull JMenuBar buildMenuBar(@NotNull BowserView bowserView) {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu(RESOURCES.getString(RESOURCE_FILE));
        menuBar.add(fileMenu);

        JMenuItem newTabMenuItem = new JMenuItem(RESOURCES.getString(RESOURCE_NEW_TAB), KeyEvent.VK_T);
        newTabMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_T, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        newTabMenuItem.addActionListener(new NewTabAction(bowserView));
        fileMenu.add(newTabMenuItem);

        fileMenu.addSeparator();

        JMenuItem closeTabMenuItem = new JMenuItem(RESOURCES.getString(RESOURCE_CLOSE_TAB), KeyEvent.VK_W);
        closeTabMenuItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
        closeTabMenuItem.addActionListener(new CloseTabAction(bowserView));
        fileMenu.add(closeTabMenuItem);

        return menuBar;
    }
}
