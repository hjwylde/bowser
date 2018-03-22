package com.hjwylde.bowser.ui.views;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * A generic view that wraps a component. This is the "View" part of the MVVM model. A {@link View} is designed to be a
 * single component (potentially made up of multiple smaller components) that has a well-defined purpose.
 */
public interface View {
    /**
     * Get the root Swing component of this {@link View}.
     *
     * @return the root Swing component.
     */
    @NotNull JComponent getComponent();
}
