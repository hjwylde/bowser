package com.hjwylde.bowser.ui.frames.bowser;

import com.hjwylde.bowser.ui.views.fileBrowser.FileBrowser;
import com.hjwylde.bowser.ui.views.tabbedFileBrowser.TabbedFileBrowser;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Optional;

@NotThreadSafe
final class SortByAction implements Runnable {
    private final @NotNull TabbedFileBrowser.View view;
    private final @NotNull Comparator<Path> comparator;

    SortByAction(@NotNull TabbedFileBrowser.View view, @NotNull Comparator<Path> comparator) {
        this.view = view;
        this.comparator = comparator;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void run() {
        Optional<FileBrowser.View> mFileBrowserView = view.getCurrentTab();

        mFileBrowserView.ifPresent(
                view -> view.sortDirectory(comparator)
        );
    }
}
