package com.hjwylde.bowser.ui.views.tabbedFileBrowser;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class TabbedFileBrowserTests {
    @Nested
    class Builder {
        @Test
        void returnsBuilder() {
            TabbedFileBrowser.Builder builder = TabbedFileBrowser.builder();

            assertNotNull(builder);
        }
    }

    @Nested
    class StaticClasses {
        @Nested
        class Builder {
            private final TabbedFileBrowser.Builder builder = TabbedFileBrowser.builder();
            private final com.hjwylde.bowser.io.file.FileSystemFactory factory = mock(com.hjwylde.bowser.io.file.FileSystemFactory.class);

            @Nested
            class Build {
                @Test
                void returnsTabbedFileBrowserViewWhenFileSystemFactoryPresent() {
                    builder.fileSystemFactory(factory);
                    TabbedFileBrowser.View view = builder.build();

                    assertNotNull(view);
                }

                @Test
                void throwsIllegalStateExceptionWhenFileSystemFactoryNull() {
                    assertThrows(IllegalStateException.class, () -> builder.build());
                }
            }

            @Nested
            class FileSystemFactory {
                @Test
                void returnsThis() {
                    TabbedFileBrowser.Builder result = builder.fileSystemFactory(factory);

                    assertEquals(builder, result);
                }
            }
        }
    }
}
