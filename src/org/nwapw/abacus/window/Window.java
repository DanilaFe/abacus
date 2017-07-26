package org.nwapw.abacus.window;

import org.nwapw.abacus.plugin.PluginManager;
import org.nwapw.abacus.tree.NumberReducer;
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
    private static final String SELECT_STRING = "Select";
    private static final String SYNTAX_ERR_STRING = "Syntax Error";
    private static final String NUMBER_SYSTEM_LABEL = "Number Type:";
    private static final String FUNCTION_LABEL = "Functions:";

    /**
     * The plugin manager used to retrieve functions.
     */
    private PluginManager manager;
    /**
     * The reducer used to evaluate the tree.
     */
    private NumberReducer reducer;

    /**
     * A collection of outputs from the calculator.
     */
    private String history;
    /**
     * The last output by the calculator.
     */
    private String lastOutput;

    /**
     * The panel where the output occurs.
     */
    private JPanel outputPanel;
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
    private JPanel sidePanel;
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
     * The button used to select a function.
     */
    private JButton functionSelectButton;

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

        history = "";
        lastOutput = "";

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(640, 480);

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

        outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());
        outputPanel.add(historyScroll, BorderLayout.CENTER);
        outputPanel.add(lastOutputArea, BorderLayout.SOUTH);

        numberSystemList = new JComboBox<>();

        numberSystemPanel = new JPanel();
        numberSystemPanel.setLayout(new BorderLayout());
        numberSystemPanel.add(new JLabel(NUMBER_SYSTEM_LABEL), BorderLayout.NORTH);
        numberSystemPanel.add(numberSystemList, BorderLayout.CENTER);

        functionList = new JComboBox<>();
        functionSelectButton = new JButton(SELECT_STRING);

        functionSelectPanel = new JPanel();
        functionSelectPanel.setLayout(new BorderLayout());
        functionSelectPanel.add(new JLabel(FUNCTION_LABEL), BorderLayout.NORTH);
        functionSelectPanel.add(functionList, BorderLayout.CENTER);
        functionSelectPanel.add(functionSelectButton, BorderLayout.SOUTH);

        sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.add(numberSystemPanel, BorderLayout.NORTH);
        sidePanel.add(functionSelectPanel, BorderLayout.SOUTH);

        JTabbedPane pane = new JTabbedPane();
        pane.add("Calculator", outputPanel);
        pane.add("Settings", sidePanel);
        add(pane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        ActionListener actionListener = (event) -> {
            TreeNode parsedExpression = TreeNode.fromString(inputField.getText());
            if(parsedExpression == null){
                lastOutputArea.setText(SYNTAX_ERR_STRING);
                return;
            }
            lastOutput = parsedExpression.reduce(reducer).toString();
            history += (history.length() == 0) ? "" : "\n\n";
            history += lastOutput;

            historyModel.addEntry(new HistoryTableModel.HistoryEntry(inputField.getText(), parsedExpression, lastOutput));
            historyTable.invalidate();
            lastOutputArea.setText(lastOutput);
        };
        inputEnterButton.addActionListener(actionListener);
        inputField.addActionListener(actionListener);
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
