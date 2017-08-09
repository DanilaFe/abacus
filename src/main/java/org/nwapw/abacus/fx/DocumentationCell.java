package org.nwapw.abacus.fx;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;
import org.nwapw.abacus.function.Documentation;

public class DocumentationCell extends ListCell<Documentation> {

    private Label codeNameLabel;
    private Label nameLabel;
    private Label description;
    private Label longDescription;
    private TitledPane titledPane;

    public DocumentationCell(){
        VBox vbox = new VBox();
        vbox.setSpacing(10);
        titledPane = new TitledPane();
        codeNameLabel = new Label();
        nameLabel = new Label();
        description = new Label();
        longDescription = new Label();
        codeNameLabel.setWrapText(true);
        nameLabel.setWrapText(true);
        description.setWrapText(true);
        longDescription.setWrapText(true);
        vbox.getChildren().add(codeNameLabel);
        vbox.getChildren().add(nameLabel);
        vbox.getChildren().add(description);
        vbox.getChildren().add(longDescription);
        titledPane.textProperty().bindBidirectional(codeNameLabel.textProperty());
        titledPane.setContent(vbox);
        titledPane.setExpanded(false);
        titledPane.prefWidthProperty().bind(widthProperty().subtract(32));

        visibleProperty().addListener((a, b, c) -> titledPane.setExpanded(false));
    }

    @Override
    protected void updateItem(Documentation item, boolean empty) {
        super.updateItem(item, empty);
        if(empty){
            codeNameLabel.setText("");
            nameLabel.setText("");
            description.setText("");
            longDescription.setText("");
            setGraphic(null);
        } else {
            codeNameLabel.setText(item.getCodeName());
            nameLabel.setText(item.getName());
            description.setText(item.getDescription());
            longDescription.setText(item.getLongDescription());
            setGraphic(titledPane);
        }
        titledPane.setExpanded(false);
    }
}
