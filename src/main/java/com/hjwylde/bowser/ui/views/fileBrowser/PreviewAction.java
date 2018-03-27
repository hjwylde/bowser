package com.hjwylde.bowser.ui.views.fileBrowser;

import com.hjwylde.bowser.ui.dialogs.FilePreviewDialog;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.nio.file.Path;

final class PreviewAction extends AbstractAction {
    PreviewAction() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent event) {
        if (!(event.getSource() instanceof JTree)) {
            throw new IllegalArgumentException("event source must be an instance of JTree, instead it was " + event.getSource().getClass());
        }

        JTree tree = (JTree) event.getSource();
        FileTreeNode node = (FileTreeNode) tree.getLastSelectedPathComponent();

        Path file = node.getFilePath();
        // TODO (hjw): This is an expensive operation, it should be backgrounded.
        previewFile(file, getFrame(tree));
    }

    private @NotNull JFrame getFrame(@NotNull Component component) {
        return (JFrame) SwingUtilities.getWindowAncestor(component);
    }

    private void previewFile(@NotNull Path file, @NotNull JFrame parent) {
        FilePreviewDialog filePreviewDialog = FilePreviewDialog.builder()
                .parent(parent)
                .file(file)
                .build();

        filePreviewDialog.show();
    }
}
