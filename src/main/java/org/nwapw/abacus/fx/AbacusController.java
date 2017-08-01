package org.nwapw.abacus.fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.nwapw.abacus.Abacus;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.tree.TreeNode;


public class AbacusController {

    private static final String ERR_SYNTAX = "Syntax Error";
    private static final String ERR_EVAL = "Evaluation Error";

    @FXML
    private TableView<HistoryModel> historyTable;
    @FXML
    private TableColumn<HistoryModel, String> inputColumn;
    @FXML
    private TableColumn<HistoryModel, String> parsedColumn;
    @FXML
    private TableColumn<HistoryModel, String> outputColumn;
    @FXML
    private Text outputText;
    @FXML
    private TextField inputField;
    @FXML
    private Button inputButton;

    private ObservableList<HistoryModel> historyData;

    private Abacus abacus;

    @FXML
    public void initialize(){
        Callback<TableColumn<HistoryModel, String>, TableCell<HistoryModel, String>> cellFactory =
                param -> new CopyableCell<>();

        abacus = new Abacus();
        historyData = FXCollections.observableArrayList();
        historyTable.setItems(historyData);
        historyTable.getSelectionModel().setCellSelectionEnabled(true);
        inputColumn.setCellFactory(cellFactory);
        inputColumn.setCellValueFactory(cell -> cell.getValue().inputProperty());
        parsedColumn.setCellFactory(cellFactory);
        parsedColumn.setCellValueFactory(cell -> cell.getValue().parsedProperty());
        outputColumn.setCellFactory(cellFactory);
        outputColumn.setCellValueFactory(cell -> cell.getValue().outputProperty());
    }

    @FXML
    private void performCalculation(){
        inputButton.setDisable(true);
        TreeNode constructedTree = abacus.parseString(inputField.getText());
        if(constructedTree == null){
            outputText.setText(ERR_SYNTAX);
            inputButton.setDisable(false);
            return;
        }
        NumberInterface evaluatedNumber = abacus.evaluateTree(constructedTree);
        if(evaluatedNumber == null){
            outputText.setText(ERR_EVAL);
            inputButton.setDisable(false);
            return;
        }
        outputText.setText(evaluatedNumber.toString());
        historyData.add(new HistoryModel(inputField.getText(), constructedTree.toString(), evaluatedNumber.toString()));

        inputButton.setDisable(false);
        inputField.setText("");
    }

}
