package org.nwapw.abacus.fx;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import org.nwapw.abacus.function.Documentation;

public class DocumentationCell extends ListCell<Documentation> {

    private Label codeNameLabel;
    private Label nameLabel;
    private Label description;
    private Label longDescription;

    public DocumentationCell(){
        codeNameLabel = new Label();
        nameLabel = new Label();
        description = new Label();
        longDescription = new Label();
    }

    @Override
    protected void updateItem(Documentation item, boolean empty) {
        super.updateItem(item, empty);
        if(empty){
            codeNameLabel.setText("");
            nameLabel.setText("");
            description.setText("");
            longDescription.setText("");
        } else {
            codeNameLabel.setText(item.getCodeName());
            nameLabel.setText(item.getName());
            description.setText(item.getDescription());
            longDescription.setText(item.getLongDescription());
        }
    }
}
