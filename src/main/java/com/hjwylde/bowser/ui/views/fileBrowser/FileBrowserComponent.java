package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.ui.views.fileDirectory.FileDirectory;
import com.hjwylde.bowser.ui.views.filePreview.FilePreview;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;

import javax.annotation.concurrent.NotThreadSafe;
import javax.swing.*;
import java.nio.file.Path;
import java.util.function.Consumer;

@NotThreadSafe
final class FileBrowserComponent implements FileBrowser.View {
    private static final @NotNull Logger LOGGER = LogManager.getLogger(FileBrowserComponent.class.getSimpleName());

    private final @NotNull JComponent root;

    private final @NotNull FileDirectory.View fileDirectoryView;
    private final @NotNull FilePreview.View filePreviewView;

    FileBrowserComponent(@NotNull JComponent root, @NotNull FileDirectory.View fileDirectoryView,
                         @NotNull FilePreview.View filePreviewView) {
        this.root = root;
        this.fileDirectoryView = fileDirectoryView;
        this.filePreviewView = filePreviewView;

        initialiseFilePreviewListener();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addDirectoryChangeListener(@NotNull Consumer<Path> listener) {
        fileDirectoryView.addDirectoryChangeListener(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull JComponent getComponent() {
        return root;
    }

    private void initialiseFilePreviewListener() {
        fileDirectoryView.addSelectedPathChangeListener(mPath -> {
            if (mPath.isPresent()) {
                filePreviewView.setFile(mPath.get());
            } else {
                filePreviewView.clearFile();
            }
        });
    }
}
