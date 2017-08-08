package org.nwapw.abacus.fx;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.nwapw.abacus.Abacus;
import org.nwapw.abacus.config.Configuration;
import org.nwapw.abacus.number.ComputationInterruptedException;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.plugin.ClassFinder;
import org.nwapw.abacus.plugin.PluginListener;
import org.nwapw.abacus.plugin.PluginManager;
import org.nwapw.abacus.plugin.StandardPlugin;
import org.nwapw.abacus.tree.TreeNode;

import java.io.File;
import java.io.IOException;
import java.util.Set;


/**
 * The controller for the abacus FX UI, responsible
 * for all the user interaction.
 */
public class AbacusController implements PluginListener {

    /**
     * The file used for saving and loading configuration.
     */
    public static final File CONFIG_FILE = new File("config.toml");
    /**
     * The title for the apply alert dialog.
     */
    private static final String APPLY_MSG_TITLE = "\"Apply\" Needed";
    /**
     * The text for the header of the apply alert dialog.
     */
    private static final String APPLY_MSG_HEADER = "The settings have not been applied.";
    /**
     * The text for the dialog that is shown if settings haven't been applied.
     */
    private static final String APPLY_MSG_TEXT = "You have made changes to the configuration, however, you haven't pressed \"Apply\". " +
            "The changes to the configuration will not be present in the calculator until \"Apply\" is pressed.";
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
    /**
     * Constant string that is displayed if the calculations are interrupted by an exception.
     */
    private static final String ERR_EXCEPTION = "Exception Thrown";
    @FXML
    private TabPane coreTabPane;
    @FXML
    private Tab calculateTab;
    @FXML
    private Tab settingsTab;
    @FXML
    private Tab functionListTab;
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
    private Button stopButton;
    @FXML
    private ComboBox<String> numberImplementationBox;
    @FXML
    private ListView<ToggleablePlugin> enabledPluginView;
    @FXML
    private TextField computationLimitField;
    @FXML
    private ListView<String> functionListView;
    @FXML
    private TextField functionListSearchField;

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
     * The list of plugin objects that can be toggled on and off,
     * and, when reloaded, get added to the plugin manager's black list.
     */
    private ObservableList<ToggleablePlugin> enabledPlugins;
    /**
     * The list of functions that are registered in the calculator.
     */
    private ObservableList<String> functionList;
    /**
     * The filtered list displayed to the user.
     */
    private FilteredList<String> functionFilter;

    /**
     * The abacus instance used for changing the plugin configuration.
     */
    private Abacus abacus;

    /**
     * Boolean which represents whether changes were made to the configuration.
     */
    private boolean changesMade;
    /**
     * Whether an alert about changes to the configuration was already shown.
     */
    private boolean reloadAlertShown;
    /**
     * The alert shown when a press to "apply" is needed.
     */
    private Alert reloadAlert;
    /**
     * The runnable that takes care of killing computations that take too long.
     */
    private final Runnable TIMER_RUNNABLE = () -> {
        try {
            Configuration abacusConfig = abacus.getConfiguration();
            if(abacusConfig.getComputationDelay() == 0) return;
            Thread.sleep((long) (abacusConfig.getComputationDelay() * 1000));
            performStop();
        } catch (InterruptedException e) { }
    };
    /**
     * The runnable used to perform the calculation.
     */
    private final Runnable CALCULATION_RUNNABLE = new Runnable() {

        private String attemptCalculation(){
            try {
                TreeNode constructedTree = abacus.parseString(inputField.getText());
                if (constructedTree == null) {
                    return ERR_SYNTAX;
                }
                NumberInterface evaluatedNumber = abacus.evaluateTree(constructedTree);
                if (evaluatedNumber == null) {
                    return ERR_EVAL;
                }
                String resultingString = evaluatedNumber.toString();
                historyData.add(new HistoryModel(inputField.getText(), constructedTree.toString(), resultingString));
                return resultingString;
            } catch (ComputationInterruptedException exception) {
                return ERR_STOP;
            } catch (RuntimeException exception){
                exception.printStackTrace();
                return ERR_EXCEPTION;
            }
        }

        @Override
        public void run() {
            String calculation = attemptCalculation();
            Platform.runLater(() -> {
                outputText.setText(calculation);
                inputField.setText("");
                inputButton.setDisable(false);
                stopButton.setDisable(true);
            });
        }
    };
    /**
     * The thread that is waiting to pause the calculation.
     */
    private Thread computationLimitThread;
    /**
     * The thread in which the computation runs.
     */
    private Thread calculationThread;

    /**
     * Alerts the user if the changes they made
     * have not yet been applied.
     */
    private void alertIfApplyNeeded(boolean ignorePrevious) {
        if (changesMade && (!reloadAlertShown || ignorePrevious)) {
            reloadAlertShown = true;
            reloadAlert.showAndWait();
        }
    }

    @FXML
    public void initialize() {
        Callback<TableColumn<HistoryModel, String>, TableCell<HistoryModel, String>> cellFactory =
                param -> new CopyableCell<>();
        Callback<ListView<ToggleablePlugin>, ListCell<ToggleablePlugin>> pluginCellFactory =
                param -> new CheckBoxListCell<>(ToggleablePlugin::enabledProperty, new StringConverter<ToggleablePlugin>() {
                    @Override
                    public String toString(ToggleablePlugin object) {
                        return object.getClassName().substring(object.getClassName().lastIndexOf('.') + 1);
                    }

                    @Override
                    public ToggleablePlugin fromString(String string) {
                        return new ToggleablePlugin(true, string);
                    }
                });

        functionList = FXCollections.observableArrayList();
        functionFilter = new FilteredList<>(functionList, (s) -> true);
        functionListView.setItems(functionFilter);
        functionListSearchField.textProperty().addListener((observable, oldValue, newValue) ->
                functionFilter.setPredicate((newValue.length() == 0) ? ((s) -> true) : ((s) -> s.contains(newValue))));
        historyData = FXCollections.observableArrayList();
        historyTable.setItems(historyData);
        numberImplementationOptions = FXCollections.observableArrayList();
        numberImplementationBox.setItems(numberImplementationOptions);
        numberImplementationBox.getSelectionModel().selectedIndexProperty().addListener(e -> changesMade = true);
        historyTable.getSelectionModel().setCellSelectionEnabled(true);
        enabledPlugins = FXCollections.observableArrayList();
        enabledPluginView.setItems(enabledPlugins);
        enabledPluginView.setCellFactory(pluginCellFactory);
        inputColumn.setCellFactory(cellFactory);
        inputColumn.setCellValueFactory(cell -> cell.getValue().getInputProperty());
        parsedColumn.setCellFactory(cellFactory);
        parsedColumn.setCellValueFactory(cell -> cell.getValue().getParsedProperty());
        outputColumn.setCellFactory(cellFactory);
        outputColumn.setCellValueFactory(cell -> cell.getValue().getOutputProperty());
        coreTabPane.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (oldValue.equals(settingsTab)) alertIfApplyNeeded(true);
        });

        abacus = new Abacus(new Configuration(CONFIG_FILE));
        PluginManager abacusPluginManager = abacus.getPluginManager();
        abacusPluginManager.addListener(this);
        abacusPluginManager.addInstantiated(new StandardPlugin(abacus.getPluginManager()));
        try {
            ClassFinder.loadJars("plugins").forEach(abacusPluginManager::addClass);
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        abacusPluginManager.reload();

        computationLimitField.setText(Double.toString(abacus.getConfiguration().getComputationDelay()));
        computationLimitField.textProperty().addListener((observable, oldValue, newValue) -> {
            if(!newValue.matches("(\\d+(\\.\\d*)?)?")) {
                computationLimitField.setText(oldValue);
            } else {
                changesMade = true;
            }
        });

        changesMade = false;
        reloadAlertShown = false;

        reloadAlert = new Alert(Alert.AlertType.WARNING);
        reloadAlert.setTitle(APPLY_MSG_TITLE);
        reloadAlert.setHeaderText(APPLY_MSG_HEADER);
        reloadAlert.setContentText(APPLY_MSG_TEXT);
    }

    @FXML
    public void performCalculation() {
        inputButton.setDisable(true);
        stopButton.setDisable(false);
        calculationThread = new Thread(CALCULATION_RUNNABLE);
        calculationThread.start();
        computationLimitThread = new Thread(TIMER_RUNNABLE);
        computationLimitThread.start();
    }

    @FXML
    public void performStop(){
        if(calculationThread != null) {
            calculationThread.interrupt();
            calculationThread = null;
        }
        if(computationLimitThread != null){
            computationLimitThread.interrupt();
            computationLimitThread = null;
        }
    }

    @FXML
    public void performSaveAndReload() {
        performSave();
        performReload();
        changesMade = false;
        reloadAlertShown = false;
    }

    @FXML
    public void performReload() {
        alertIfApplyNeeded(true);
        abacus.getPluginManager().reload();
    }

    @FXML
    public void performSave() {
        Configuration configuration = abacus.getConfiguration();
        configuration.setNumberImplementation(numberImplementationBox.getSelectionModel().getSelectedItem());
        Set<String> disabledPlugins = configuration.getDisabledPlugins();
        disabledPlugins.clear();
        for (ToggleablePlugin pluginEntry : enabledPlugins) {
            if (!pluginEntry.isEnabled()) disabledPlugins.add(pluginEntry.getClassName());
        }
        if(computationLimitField.getText().matches("\\d*(\\.\\d+)?") && computationLimitField.getText().length() != 0)
            configuration.setComputationDelay(Double.parseDouble(computationLimitField.getText()));
        configuration.saveTo(CONFIG_FILE);
        changesMade = false;
        reloadAlertShown = false;
    }

    @Override
    public void onLoad(PluginManager manager) {
        Configuration configuration = abacus.getConfiguration();
        Set<String> disabledPlugins = configuration.getDisabledPlugins();
        numberImplementationOptions.addAll(abacus.getPluginManager().getAllNumberImplementations());
        String actualImplementation = configuration.getNumberImplementation();
        String toSelect = (numberImplementationOptions.contains(actualImplementation)) ? actualImplementation : "<default>";
        numberImplementationBox.getSelectionModel().select(toSelect);
        for (Class<?> pluginClass : abacus.getPluginManager().getLoadedPluginClasses()) {
            String fullName = pluginClass.getName();
            ToggleablePlugin plugin = new ToggleablePlugin(!disabledPlugins.contains(fullName), fullName);
            plugin.enabledProperty().addListener(e -> changesMade = true);
            enabledPlugins.add(plugin);
        }
        functionList.addAll(manager.getAllFunctions());
        functionList.sort(String::compareTo);
    }

    @Override
    public void onUnload(PluginManager manager) {
        functionList.clear();
        enabledPlugins.clear();
        numberImplementationOptions.clear();
    }

}
