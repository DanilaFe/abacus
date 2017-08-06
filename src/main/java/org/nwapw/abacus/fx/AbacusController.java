package org.nwapw.abacus.fx;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxListCell;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.util.StringConverter;
import org.nwapw.abacus.Abacus;
import org.nwapw.abacus.config.Configuration;
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
    @FXML
    private TabPane coreTabPane;
    @FXML
    private Tab calculateTab;
    @FXML
    private Tab settingsTab;
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
    private ListView<ToggleablePlugin> enabledPluginView;

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
     * <<<<<<< HEAD
     * The list of plugin objects that can be toggled on and off,
     * and, when reloaded, get added to the plugin manager's black list.
     */
    private ObservableList<ToggleablePlugin> enabledPlugins;

    /**
     * The abacus instance used for changing the plugin configuration.
     */
    private Abacus abacus;
    /**
     * Thread used for calculating.
     */
    private Thread calcThread;

    /**
     * Checks whether the calculator is calculating.
     */
    private boolean calculating;

    /**
     * Seconds delayed for timer;
     */
    private double delay = 0;

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
        inputColumn.setCellValueFactory(cell -> cell.getValue().inputProperty());
        parsedColumn.setCellFactory(cellFactory);
        parsedColumn.setCellValueFactory(cell -> cell.getValue().parsedProperty());
        outputColumn.setCellFactory(cellFactory);
        outputColumn.setCellValueFactory(cell -> cell.getValue().outputProperty());
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

        changesMade = false;
        reloadAlertShown = false;

        reloadAlert = new Alert(Alert.AlertType.WARNING);
        reloadAlert.setTitle(APPLY_MSG_TITLE);
        reloadAlert.setHeaderText(APPLY_MSG_HEADER);
        reloadAlert.setContentText(APPLY_MSG_TEXT);
    }

    @FXML
    private void performCalculation() {
        Runnable calculator = new Runnable() {
            public void run() {
                if (delay > 0) {
                    Runnable timer = new Runnable() {
                        public void run() {
                            long gap = (long) (delay * 1000);
                            long startTime = System.currentTimeMillis();
                            while (System.currentTimeMillis() - startTime <= gap) {
                            }
                            stopCalculation();
                        }
                    };
                    Thread maxTime = new Thread(timer);
                    maxTime.setName("maxTime");
                    maxTime.start();
                }
                calculating = true;
                Platform.runLater(() -> inputButton.setDisable(true));
                TreeNode constructedTree = abacus.parseString(inputField.getText());
                if (constructedTree == null) {
                    Platform.runLater(() -> outputText.setText(ERR_SYNTAX));
                    Platform.runLater(() -> inputButton.setDisable(false));
                    //return;
                } else {
                    NumberInterface evaluatedNumber = abacus.evaluateTree(constructedTree);
                    if (evaluatedNumber == null) {
                        if (Thread.currentThread().isInterrupted()) {
                            Platform.runLater(() -> outputText.setText(ERR_STOP));
                            Platform.runLater(() -> inputButton.setDisable(false));
                        } else {
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
        if (!calculating) {
            calcThread = new Thread(calculator);
            calcThread.setName("calcThread");
            calcThread.start();
        }
    }

    @FXML
    private void stopCalculation() {
        calcThread.interrupt();
        calculating = false;
        //Platform.runLater(() ->inputButton.setDisable(false));
    }

    @FXML
    private void performSaveAndReload() {
        performSave();
        performReload();
        changesMade = false;
        reloadAlertShown = false;
    }

    @FXML
    private void performReload() {
        alertIfApplyNeeded(true);
        abacus.getPluginManager().reload();
    }

    @FXML
    private void performSave() {
        Configuration configuration = abacus.getConfiguration();
        configuration.setNumberImplementation(numberImplementationBox.getSelectionModel().getSelectedItem());
        Set<String> disabledPlugins = configuration.getDisabledPlugins();
        disabledPlugins.clear();
        for (ToggleablePlugin pluginEntry : enabledPlugins) {
            if (!pluginEntry.isEnabled()) disabledPlugins.add(pluginEntry.getClassName());
        }
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
    }

    @Override
    public void onUnload(PluginManager manager) {
        enabledPlugins.clear();
        numberImplementationOptions.clear();
    }
}
