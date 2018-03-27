package com.hjwylde.bowser.ui.views.scrollable;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class ScrollableTests {
    @Nested
    class Builder {
        @Test
        void returnsBuilder() {
            Scrollable.Builder builder = Scrollable.builder();

            assertNotNull(builder);
        }
    }

    @Nested
    class StaticClasses {
        @Nested
        class Builder {
            private Scrollable.Builder builder = Scrollable.builder();
            private Scrollable.View view = mock(Scrollable.View.class);

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
                    assertThrows(IllegalStateException.class, () -> builder.build());
                }
            }

            @Nested
            class View {
                @Test
                void returnsThis() {
                    Scrollable.Builder result = builder.view(view);

                    assertEquals(builder, result);
                }

                @Test
                void throwsNullPointerExceptionWhenNull() {
                    assertThrows(NullPointerException.class, () -> builder.view(null));
                }
            }
        }
    }
}
