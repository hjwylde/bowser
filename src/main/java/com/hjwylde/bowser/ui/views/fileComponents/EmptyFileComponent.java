package com.hjwylde.bowser.ui.views.fileComponents;

import com.hjwylde.bowser.modules.LocaleModule;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.util.ResourceBundle;

@NotThreadSafe
final class EmptyFileComponent extends TextFileComponent {
    // TODO (hjw): This couples the component with the concept of "previewing". It would be better if this (and the
    // factory) belonged with the preview package.
    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(EmptyFileComponent.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_EMPTY_LABEL = "emptyLabel";

    EmptyFileComponent() {
        super(RESOURCES.getString(RESOURCE_EMPTY_LABEL));
    }
}
