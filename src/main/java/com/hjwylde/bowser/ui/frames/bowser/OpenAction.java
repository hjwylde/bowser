package com.hjwylde.bowser.ui.frames.bowser;

import com.hjwylde.bowser.ui.dialogs.OpenDialog;
import com.hjwylde.bowser.ui.views.tabbedFileBrowser.TabbedFileBrowser;
import com.hjwylde.bowser.util.concurrent.SwingExecutors;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.nio.file.Path;

@NotThreadSafe
final class OpenAction implements Runnable {
    private final @NotNull TabbedFileBrowser.View view;

    OpenAction(@NotNull TabbedFileBrowser.View view) {
        this.view = view;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        OpenDialog dialog = OpenDialog.builder()
                .parent(view.getComponent())
                .build();

        int result = dialog.show();
        if (result != OpenDialog.OK_OPTION) {
            return;
        }

        SwingExecutors.edt().execute(() -> {
            Path path = dialog.getPath();

            view.addTab(path);
        });
    }
}
