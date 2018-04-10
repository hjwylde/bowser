package com.hjwylde.bowser.ui.views.tabbedFileBrowser;

import com.google.common.jimfs.Jimfs;
import com.hjwylde.bowser.io.file.FileSystemFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.FileSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.*;

class TabbedFileBrowserComponentTests {
    private JTabbedPane tabbedPane;

    private FileSystemFactory fileSystemFactory;
    private FileSystem fileSystem;

    private TabbedFileBrowserComponent component;

    @BeforeEach
    void init() {
        tabbedPane = spy(JTabbedPane.class);
        fileSystemFactory = mock(FileSystemFactory.class);

        fileSystem = Jimfs.newFileSystem();
        when(fileSystemFactory.getFileSystem()).thenReturn(fileSystem);

        component = new TabbedFileBrowserComponent(tabbedPane, fileSystemFactory);
    }

    @AfterEach
    void tearDown() throws IOException {
        fileSystem.close();
    }

    @Nested
    class AddFtpTab {
        // TODO (hjw): This method is difficult to test. I believe this is a sign that perhaps I should inject the
        // handler (a dependency) for creating the FTP connection dialog. Or perhaps I should refactor it to take
        // arguments and let the action handler deal with how to query the host/username/password from the user.
        // The fact that calling this method currently blocks the thread with a dialog is a huge sign that something
        // needs to change here.
    }

    @Nested
    class AddTab {
        @Test
        void addsNewTab() {
            assumeTrue(tabbedPane.getTabCount() == 0);

            component.addTab();

            assertEquals(1, tabbedPane.getTabCount());
            assertNotNull(tabbedPane.getSelectedComponent());
        }

        @Test
        void getsFileSystemFromFactory() {
            component.addTab();

            verify(fileSystemFactory).getFileSystem();
        }
    }

    @Nested
    class GetComponent {
        @Test
        void returnsComponent() {
            JComponent result = component.getComponent();

            assertEquals(tabbedPane, result);
        }
    }

    @Nested
    class RemoveCurrentTab {
        @Test
        void doesNothingWhenNoTabs() {
            component.removeCurrentTab();

            assertEquals(0, tabbedPane.getTabCount());
        }

        @Test
        void removesCurrentTab() {
            component.addTab();

            assumeTrue(tabbedPane.getTabCount() > 0);

            component.removeCurrentTab();

            assertEquals(0, tabbedPane.getTabCount());
        }
    }
}
