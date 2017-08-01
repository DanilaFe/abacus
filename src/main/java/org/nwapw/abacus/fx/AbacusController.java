package org.nwapw.abacus.fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.nwapw.abacus.Abacus;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.tree.TreeNode;

public class AbacusController {

    private static final String ERR_SYNTAX = "Syntax Error";
    private static final String ERR_EVAL = "Evaluation Error";

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
        abacus = new Abacus();
        historyData = FXCollections.observableArrayList();
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
        inputButton.setDisable(false);
        outputText.setText(evaluatedNumber.toString());
    }

}
