package org.nwapw.abacus.fx;

import javafx.scene.control.TableCell;
import javafx.scene.input.MouseEvent;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class CopyableCell<S, T> extends TableCell<S, T> {

    public CopyableCell(){
        addEventFilter(MouseEvent.MOUSE_CLICKED, event -> {
            if(event.getClickCount() == 2){
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
