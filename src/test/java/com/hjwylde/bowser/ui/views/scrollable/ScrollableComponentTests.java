package com.hjwylde.bowser.ui.views.scrollable;

import com.hjwylde.bowser.ui.views.View;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

class ScrollableComponentTests {
    private View view = mock(View.class);
    private ScrollableComponent component = new ScrollableComponent(view);

    @Nested
    class GetComponent {
        @Test
        void returnsWrapperAroundViewComponent() {
            JComponent wrapper = component.getComponent();
            // TODO (hjw): Is there a better way to do this? Method chaining and casting will make this test brittle.
            JViewport viewport = ((JScrollPane) wrapper).getViewport();

            assertEquals(view.getComponent(), viewport.getView());
        }
    }
}
