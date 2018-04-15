package com.hjwylde.bowser.ui.views.tabbedFileBrowser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class TabbedFileBrowserTests {
    @Nested
    class StaticClasses {
        @Nested
        class Builder {
            private TabbedFileBrowser.Builder builder;
            private com.hjwylde.bowser.io.file.FileSystemFactory factory;

            @BeforeEach
            void init() {
                builder = TabbedFileBrowser.builder();
                factory = mock(com.hjwylde.bowser.io.file.FileSystemFactory.class);
            }

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
                    assertThrows(IllegalStateException.class, builder::build);
                }
            }
        }
    }
}
