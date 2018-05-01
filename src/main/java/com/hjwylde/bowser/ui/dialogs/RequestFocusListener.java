package com.hjwylde.bowser.ui.dialogs;

import javax.annotation.concurrent.Immutable;
import javax.swing.event.AncestorEvent;
import javax.swing.event.AncestorListener;
import java.awt.*;

/**
 * An {@link AncestorListener} that immediately requests focus when an ancestor is added to this component. This is
 * useful in dialogs to request focus on input fields.
 */
@Immutable
final class RequestFocusListener implements AncestorListener {
    /**
     * {@inheritDoc}
     */
    @Override
    public void ancestorAdded(AncestorEvent event) {
        Component component = event.getComponent();
        component.requestFocusInWindow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ancestorMoved(AncestorEvent event) {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void ancestorRemoved(AncestorEvent event) {
    }
}
