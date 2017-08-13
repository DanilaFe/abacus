package org.nwapw.abacus.fx;

import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

/**
 * A cell that copies its value to the clipboard
 * when double clicked.
 *
 * @param <S> The type of the table view generic type.
 * @param <T> The type of the value contained in the cell.
 */
public class CopyableCell<S, T> extends TableCell<S, T> {

    /**
     * Creates a new copyable cell.
     */
    public CopyableCell() {
        addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if (event.getClickCount() == 2) {
                Toolkit.getDefaultToolkit().getSystemClipboard()
                        .setContents(new StringSelection(getText()), null);
            }
        });
    }

    @Override
    protected void updateItem(T item, boolean empty) {
        super.updateItem(item, empty);
        setText((empty || item == null) ? null : item.toString());
        setGraphic(null);
    }
}
