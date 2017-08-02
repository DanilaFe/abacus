package org.nwapw.abacus.fx;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.nwapw.abacus.Abacus;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.plugin.ClassFinder;
import org.nwapw.abacus.tree.TreeNode;

import java.io.IOException;


/**
 * The controller for the abacus FX UI, responsible
 * for all the user interaction.
 */
public class AbacusController {

    /**
     * Constant string that is displayed if the text could not be lexed or parsed.
     */
    private static final String ERR_SYNTAX = "Syntax Error";
    /**
     * Constant string that is displayed if the tree could not be reduced.
     */
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
    @FXML
    private ComboBox<String> numberImplementationBox;
    @FXML
    private Button loadButton;
    @FXML
    private Button unloadButton;
    @FXML
    private TextField loadField;

    /**
     * The list of history entries, created by the users.
     */
    private ObservableList<HistoryModel> historyData;

    /**
     * The abacus instance used for calculations and all
     * other main processing code.
     */
    private ObservableList<String> numberImplementationOptions;

    private Abacus abacus;

    @FXML
    public void initialize(){
        Callback<TableColumn<HistoryModel, String>, TableCell<HistoryModel, String>> cellFactory =
                param -> new CopyableCell<>();

        historyData = FXCollections.observableArrayList();
        historyTable.setItems(historyData);
        numberImplementationOptions = FXCollections.observableArrayList();
        numberImplementationBox.setItems(numberImplementationOptions);
        numberImplementationBox.valueProperty().addListener((observable, oldValue, newValue)
                -> {
            abacus.getConfiguration().setNumberImplementation(newValue);
            abacus.getConfiguration().saveTo(Abacus.CONFIG_FILE);
        });
        historyTable.getSelectionModel().setCellSelectionEnabled(true);
        inputColumn.setCellFactory(cellFactory);
        inputColumn.setCellValueFactory(cell -> cell.getValue().inputProperty());
        parsedColumn.setCellFactory(cellFactory);
        parsedColumn.setCellValueFactory(cell -> cell.getValue().parsedProperty());
        outputColumn.setCellFactory(cellFactory);
        outputColumn.setCellValueFactory(cell -> cell.getValue().outputProperty());

        abacus = new Abacus();
        numberImplementationOptions.addAll(abacus.getPluginManager().getAllNumbers());
        String actualImplementation = abacus.getConfiguration().getNumberImplementation();
        String toSelect = (numberImplementationOptions.contains(actualImplementation)) ? actualImplementation : "naive";
        numberImplementationBox.getSelectionModel().select(toSelect);
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
    @FXML
    private void loadClass(){
        try {
            for(Class<?> plugin :ClassFinder.loadJars("plugins")){
                String name = "";
                //String name = plugin.getName();
                while(!(name.indexOf('/') ==-1)){
                    name=name.substring(name.indexOf('/')+1);
                }
                if(loadField.getText().equals("")||loadField.getText().equals(name)||(loadField.getText()+".class").equals(name)){
                    //abacus.loadClass(plugin);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void unloadClass(){
        try {
            for(Class<?> plugin :ClassFinder.loadJars("plugins")){
                String name = plugin.getName();
                while(!(name.indexOf('/') ==-1)){
                    name=name.substring(name.indexOf('/')+1);
                }
                if(loadField.getText().equals("")||loadField.getText().equals(name)||(loadField.getText()+".class").equals(name)){
                    //abacus.unloadClass(plugin);
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

}
