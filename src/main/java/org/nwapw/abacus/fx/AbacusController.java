package org.nwapw.abacus.fx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;
import org.nwapw.abacus.Abacus;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.tree.TreeNode;


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
    /**
     * Constant string that is displayed if the calculations are stopped before they are done.
     */
    private static final String ERR_STOP = "Stopped";
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

    /**
     * The list of history entries, created by the users.
     */
    private ObservableList<HistoryModel> historyData;

    /**
     * The abacus instance used for calculations and all
     * other main processing code.
     */
    private ObservableList<String> numberImplementationOptions;

    /**
     * Thread used for calculating.
     */
    private Thread calcThread;

    /**
     * Checks whether the calculator is calculating.
     */
    private boolean calculating;

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
        Runnable calculator = new Runnable(){
            public void run() {
                calculating = true;
                Platform.runLater(() -> inputButton.setDisable(true));
                TreeNode constructedTree = abacus.parseString(inputField.getText());
                if (constructedTree == null) {
                    Platform.runLater(() ->outputText.setText(ERR_SYNTAX));
                    Platform.runLater(() -> inputButton.setDisable(false));
                    //return;
                }else {
                    NumberInterface evaluatedNumber = abacus.evaluateTree(constructedTree);
                    if (evaluatedNumber == null) {
                        if(Thread.currentThread().isInterrupted()){
                            Platform.runLater(() -> outputText.setText(ERR_STOP));
                            Platform.runLater(() -> inputButton.setDisable(false));
                        }else {
                            Platform.runLater(() -> outputText.setText(ERR_EVAL));
                            Platform.runLater(() -> inputButton.setDisable(false));
                            //return;
                        }
                    } else {
                        Platform.runLater(() -> outputText.setText(evaluatedNumber.toString()));

                        historyData.add(new HistoryModel(inputField.getText(), constructedTree.toString(), evaluatedNumber.toString()));

                        Platform.runLater(() -> inputButton.setDisable(false));
                        Platform.runLater(() -> inputField.setText(""));
                    }
                }
                calculating = false;
            }
        };
        if(!calculating) {
                calcThread = new Thread(calculator);
                calcThread.start();
        }
    }
    @FXML
    private void stopCalculation(){
        calcThread.interrupt();
        calculating = false;
        inputButton.setDisable(false);
    }

}
