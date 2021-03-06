package com.hjwylde.bowser.ui.views.tabbedFileBrowser;

import com.google.common.jimfs.Jimfs;
import com.hjwylde.bowser.io.file.FileSystemFactory;
import org.junit.jupiter.api.*;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.FileSystem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.mockito.Mockito.*;

class TabbedFileBrowserComponentTests {
    private JComponent root;
    private JTabbedPane tabbedPane;

    private FileSystemFactory fileSystemFactory;
    private FileSystem fileSystem;

    private TabbedFileBrowserComponent component;

    @BeforeEach
    void init() {
        root = spy(JComponent.class);
        tabbedPane = spy(JTabbedPane.class);
        fileSystemFactory = mock(FileSystemFactory.class);

        fileSystem = Jimfs.newFileSystem();
        when(fileSystemFactory.getFileSystem()).thenReturn(fileSystem);

        TabbedFileBrowserViewModel viewModel = new TabbedFileBrowserViewModel();

        component = new TabbedFileBrowserComponent(root, mock(JButton.class), mock(JButton.class), tabbedPane,
                fileSystemFactory, viewModel);
    }

    @AfterEach
    void tearDown() throws IOException {
        fileSystem.close();
    }

    @Nested
    class AddTab {
        @Test
        @Disabled("Method now uses concurrency, I need to re-write this test")
        void addsNewTab() {
            assumeTrue(tabbedPane.getTabCount() == 0);

            component.addTab();

            assertEquals(1, tabbedPane.getTabCount());
            assertNotNull(tabbedPane.getSelectedComponent());
        }

        @Test
        void getsFileSystemFromFactoryWhenNoneProvided() {
            component.addTab();

            verify(fileSystemFactory).getFileSystem();
        }

        @Test
        void usesFileSystemWhenProvided() {
            component.addTab(fileSystem);

            verifyZeroInteractions(fileSystemFactory);
        }
    }

    @Nested
    class GetComponent {
        @Test
        void returnsComponent() {
            JComponent result = component.getComponent();

            assertEquals(root, result);
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
        @Disabled("Method now uses concurrency, I need to re-write this test")
        void removesCurrentTab() {
            component.addTab();

            assumeTrue(tabbedPane.getTabCount() > 0);

            component.removeCurrentTab();

            assertEquals(0, tabbedPane.getTabCount());
        }
    }
}
