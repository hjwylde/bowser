package com.hjwylde.bowser.ui.views.scrollable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;

class ScrollableTests {
    @Nested
    class StaticClasses {
        @Nested
        class Builder {
            private Scrollable.Builder builder;
            private Scrollable.View view;

            @BeforeEach
            void init() {
                builder = Scrollable.builder();
                view = mock(Scrollable.View.class);
            }

            @Nested
            class Build {
                @Test
                void returnsScrollableViewWhenViewPresent() {
                    builder.view(view);
                    Scrollable.View view = builder.build();

                    assertNotNull(view);
                }

                @Test
                void throwsIllegalStateExceptionWhenViewNull() {
                    assertThrows(IllegalStateException.class, builder::build);
                }
            }
        }
    }
}
