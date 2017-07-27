package org.nwapw.abacus.window;

import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.plugin.PluginManager;
import org.nwapw.abacus.tree.NumberReducer;
import org.nwapw.abacus.tree.TreeBuilder;
import org.nwapw.abacus.tree.TreeNode;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * The main UI window for the calculator.
 */
public class Window extends JFrame {

    private static final String CALC_STRING = "Calculate";
    private static final String SYNTAX_ERR_STRING = "Syntax Error";
    private static final String EVAL_ERR_STRING = "Evaluation Error";
    private static final String NUMBER_SYSTEM_LABEL = "Number Type:";
    private static final String FUNCTION_LABEL = "Functions:";

    /**
     * Array of Strings to which the "calculate" button's text
     * changes. For instance, in the graph tab, the name will
     * be "Graph" and not "Calculate".
     */
    private static final String[] BUTTON_NAMES = {
            CALC_STRING,
            CALC_STRING
    };

    /**
     * Array of booleans that determine whether the input
     * field and the input button are enabled at a particular
     * index.
     */
    private static boolean[] INPUT_ENABLED = {
            true,
            false
    };

    /**
     * The plugin manager used to retrieve functions.
     */
    private PluginManager manager;
    /**
     * The builder used to construct the parse trees.
     */
    private TreeBuilder builder;
    /**
     * The reducer used to evaluate the tree.
     */
    private NumberReducer reducer;

    /**
     * The last output by the calculator.
     */
    private String lastOutput;

    /**
     * The tabbed pane that separates calculator contexts.
     */
    private JTabbedPane pane;

    /**
     * The panel where the output occurs.
     */
    private JPanel calculationPanel;
    /**
     * The text area reserved for the last output.
     */
    private JTextArea lastOutputArea;
    /**
     * The table used for storing history results.
     */
    private JTable historyTable;
    /**
     * The table model used for managing history.
     */
    private HistoryTableModel historyModel;
    /**
     * The scroll pane for the history area.
     */
    private JScrollPane historyScroll;

    /**
     * The panel where the input occurs.
     */
    private JPanel inputPanel;
    /**
     * The input text field.
     */
    private JTextField inputField;
    /**
     * The "submit" button.
     */
    private JButton inputEnterButton;

    /**
     * The side panel for separate configuration.
     */
    private JPanel settingsPanel;
    /**
     * Panel for elements relating to number
     * system selection.
     */
    private JPanel numberSystemPanel;
    /**
     * The possible list of number systems.
     */
    private JComboBox<String> numberSystemList;
    /**
     * The panel for elements relating to
     * function selection.
     */
    private JPanel functionSelectPanel;
    /**
     * The list of functions available to the user.
     */
    private JComboBox<String> functionList;

    /**
     * Action listener that causes the input to be evaluated.
     */
    private ActionListener evaluateListener = (event) -> {
        TreeBuilder builder = new TreeBuilder();
        TreeNode parsedExpression = builder.fromString(inputField.getText());
        if(parsedExpression == null){
            lastOutputArea.setText(SYNTAX_ERR_STRING);
            return;
        }
        NumberInterface numberInterface = parsedExpression.reduce(reducer);
        if(numberInterface == null){
            lastOutputArea.setText(EVAL_ERR_STRING);;
            return;
        }
        lastOutput = numberInterface.toString();
        historyModel.addEntry(new HistoryTableModel.HistoryEntry(inputField.getText(), parsedExpression, lastOutput));
        historyTable.invalidate();
        lastOutputArea.setText(lastOutput);
        inputField.setText("");
    };

    /**
     * Array of listeners that tell the input button how to behave
     * at a given input tab.
     */
    private ActionListener[] listeners = {
            evaluateListener,
            null
    };

    /**
     * Creates a new window with the given manager.
     * @param manager the manager to use.
     */
    public Window(PluginManager manager){
        this();
        this.manager = manager;
        reducer = new NumberReducer(manager);
    }

    /**
     * Creates a new window.
     */
    private Window() {
        super();

        lastOutput = "";

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(320, 480);

        inputField = new JTextField();
        inputEnterButton = new JButton(CALC_STRING);

        inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(inputEnterButton, BorderLayout.SOUTH);

        historyModel = new HistoryTableModel();
        historyTable = new JTable(historyModel);
        historyScroll = new JScrollPane(historyTable);
        lastOutputArea = new JTextArea(lastOutput);
        lastOutputArea.setEditable(false);

        calculationPanel = new JPanel();
        calculationPanel.setLayout(new BorderLayout());
        calculationPanel.add(historyScroll, BorderLayout.CENTER);
        calculationPanel.add(lastOutputArea, BorderLayout.SOUTH);

        numberSystemList = new JComboBox<>();

        numberSystemPanel = new JPanel();
        numberSystemPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        numberSystemPanel.setLayout(new FlowLayout());
        numberSystemPanel.add(new JLabel(NUMBER_SYSTEM_LABEL));
        numberSystemPanel.add(numberSystemList);
        numberSystemPanel.setMaximumSize(numberSystemPanel.getPreferredSize());

        functionList = new JComboBox<>();

        functionSelectPanel = new JPanel();
        functionSelectPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        functionSelectPanel.setLayout(new FlowLayout());
        functionSelectPanel.add(new JLabel(FUNCTION_LABEL));
        functionSelectPanel.add(functionList);
        functionSelectPanel.setMaximumSize(functionSelectPanel.getPreferredSize());

        settingsPanel = new JPanel();
        settingsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.PAGE_AXIS));
        settingsPanel.add(numberSystemPanel);
        settingsPanel.add(functionSelectPanel);

        pane = new JTabbedPane();
        pane.add("Calculator", calculationPanel);
        pane.add("Settings", settingsPanel);
        pane.addChangeListener(e -> {
            int selectionIndex = pane.getSelectedIndex();
            boolean enabled = INPUT_ENABLED[selectionIndex];
            ActionListener listener = listeners[selectionIndex];
            inputEnterButton.setText(BUTTON_NAMES[selectionIndex]);
            inputField.setEnabled(enabled);
            inputEnterButton.setEnabled(enabled);

            for(ActionListener removingListener : inputEnterButton.getActionListeners()){
                inputEnterButton.removeActionListener(removingListener);
                inputField.removeActionListener(removingListener);
            }
            if(listener != null){
                inputEnterButton.addActionListener(listener);
                inputField.addActionListener(listener);
            }
        });
        add(pane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        inputEnterButton.addActionListener(evaluateListener);
        inputField.addActionListener(evaluateListener);
        historyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();
                if(e.getClickCount() == 2){
                    int row = historyTable.rowAtPoint(clickPoint);
                    int column = historyTable.columnAtPoint(clickPoint);
                    String toCopy = historyTable.getValueAt(row, column).toString();
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(toCopy), null);
                }
            }
        });
    }
}
