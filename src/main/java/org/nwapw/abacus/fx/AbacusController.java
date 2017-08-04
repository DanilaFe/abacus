package org.nwapw.abacus.fx;

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
import org.nwapw.abacus.plugin.PluginListener;
import org.nwapw.abacus.plugin.PluginManager;
import org.nwapw.abacus.tree.TreeNode;

import java.util.Set;


/**
 * The controller for the abacus FX UI, responsible
 * for all the user interaction.
 */
public class AbacusController implements PluginListener {

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
     * The list of plugin objects that can be toggled on and off,
     * and, when reloaded, get added to the plugin manager's black list.
     */
    private ObservableList<ToggleablePlugin> enabledPlugins;

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
     * Alerts the user if the changes they made
     * have not yet been applied.
     */
    private void alertIfApplyNeeded(boolean ignorePrevious){
        if(changesMade && (!reloadAlertShown || ignorePrevious)) {
            reloadAlertShown = true;
            reloadAlert.showAndWait();
        }
    }

    @FXML
    public void initialize(){
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
            if(oldValue.equals(settingsTab)) alertIfApplyNeeded(true);
        });

        abacus = new Abacus();
        abacus.getPluginManager().addListener(this);
        abacus.getPluginManager().reload();

        changesMade = false;
        reloadAlertShown = false;

        reloadAlert = new Alert(Alert.AlertType.WARNING);
        reloadAlert.setTitle(APPLY_MSG_TITLE);
        reloadAlert.setHeaderText(APPLY_MSG_HEADER);
        reloadAlert.setContentText(APPLY_MSG_TEXT);
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
    private void performReload(){
        alertIfApplyNeeded(true);
        abacus.getPluginManager().reload();
    }

    @FXML
    private void performSave(){
        Configuration configuration = abacus.getConfiguration();
        configuration.setNumberImplementation(numberImplementationBox.getSelectionModel().getSelectedItem());
        Set<String> disabledPlugins = configuration.getDisabledPlugins();
        disabledPlugins.clear();
        for(ToggleablePlugin pluginEntry : enabledPlugins){
            if(!pluginEntry.isEnabled()) disabledPlugins.add(pluginEntry.getClassName());
        }
        configuration.saveTo(Abacus.CONFIG_FILE);
        changesMade = false;
        reloadAlertShown = false;
    }

    @Override
    public void onLoad(PluginManager manager) {
        Configuration configuration = abacus.getConfiguration();
        Set<String> disabledPlugins = configuration.getDisabledPlugins();
        numberImplementationOptions.addAll(abacus.getPluginManager().getAllNumbers());
        String actualImplementation = configuration.getNumberImplementation();
        String toSelect = (numberImplementationOptions.contains(actualImplementation)) ? actualImplementation : "<default>";
        numberImplementationBox.getSelectionModel().select(toSelect);
        for(Class<?> pluginClass : abacus.getPluginManager().getLoadedPluginClasses()){
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
