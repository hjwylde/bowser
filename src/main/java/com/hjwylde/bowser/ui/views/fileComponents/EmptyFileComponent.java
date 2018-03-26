package com.hjwylde.bowser.ui.views.fileComponents;

import com.hjwylde.bowser.modules.LocaleModule;
import org.jetbrains.annotations.NotNull;

import java.util.ResourceBundle;

final class EmptyFileComponent extends TextFileComponent {
    private static final @NotNull ResourceBundle RESOURCES = ResourceBundle.getBundle(EmptyFileComponent.class.getName(), LocaleModule.provideLocale());
    private static final @NotNull String RESOURCE_EMPTY_LABEL = "emptyLabel";

    EmptyFileComponent() {
        super(RESOURCES.getString(RESOURCE_EMPTY_LABEL));
    }
}
