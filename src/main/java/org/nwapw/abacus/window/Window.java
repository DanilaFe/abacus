package org.nwapw.abacus.window;

import org.nwapw.abacus.Abacus;
import org.nwapw.abacus.number.NumberInterface;
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
    
    private boolean contComputation;
    
    private static final String CALC_STRING = "Calculate";
    private static final String SYNTAX_ERR_STRING = "Syntax Error";
    private static final String EVAL_ERR_STRING = "Evaluation Error";
    private static final String NUMBER_SYSTEM_LABEL = "Number Type:";
    private static final String FUNCTION_LABEL = "Functions:";
    private static final String STOP_STRING = "Stop";
    private static final String STOPPED_TEXT = "Stopped";
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
     * The instance of the Abacus class, used
     * for interaction with plugins and configuration.
     */
    private Abacus abacus;
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
     * The stop calculations button.
     */
    private JButton inputStopButton;
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
     * Thread used for calculations
     */
    private Thread calculateThread;
    /**
     * Check if currently calculating
     */
    private boolean calculating;
    private int count;
    //private Object monitor;
    /**
     * Action listener that causes the input to be evaluated.
     */
   //*
    private ActionListener stopListener = (event) -> {
        //contComputation=false;
        //Long pause = Long.MAX_VALUE;
        //System.out.println(Thread.currentThread().getName());
        //System.out.println(calculateThread.getName());
        //calculateThread.suspend();
        System.out.println(count++);
        calculating = false;
        /*
        synchronized(calculateThread) {
            try {
                calculateThread.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        //*/
    };//*/
    private ActionListener evaluateListener = (event) -> {
        //contComputation = true;
        Runnable calculate = new Runnable() {
            public void run() {
                boolean skip = false;
                calculating = true;
                TreeNode parsedExpression = null;


                parsedExpression = abacus.parseString(inputField.getText());
                if (parsedExpression == null) {
                    lastOutputArea.setText(SYNTAX_ERR_STRING);
                    skip = true;
                }
                /*
                try {
                        Thread.currentThread().sleep(9999);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IllegalMonitorStateException e){
                    e.printStackTrace();
                }
                //*/
                //NumberInterface numberInterface = null;

                if(!skip){
                    NumberInterface numberInterface = abacus.evaluateTree(parsedExpression);
                    if (numberInterface == null) {
                        lastOutputArea.setText(EVAL_ERR_STRING);
                        return;
                    }
                    if(calculateThread.equals(Thread.currentThread())) {
                        lastOutput = numberInterface.toString();
                        historyModel.addEntry(new HistoryTableModel.HistoryEntry(inputField.getText(), parsedExpression, lastOutput));
                        historyTable.invalidate();
                        lastOutputArea.setText(lastOutput);
                        inputField.setText("");
                        calculating = false;
                    }
                }
            }
        };
        if(!calculating) {
            calculateThread = new Thread(calculate);
            calculateThread.setName("a-"+System.currentTimeMillis());
            calculateThread.start();
        }
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
     *
     * @param abacus the calculator instance to interact with other components.
     */
    public Window(Abacus abacus) {
        this();
        this.abacus = abacus;
    }

    /**
     * Creates a new window.
     */
    private Window() {
        super();

        contComputation = true;

        lastOutput = "";

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(320, 480);

        inputField = new JTextField();
        inputEnterButton = new JButton(CALC_STRING);
        inputStopButton = new JButton(STOP_STRING);

        inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputStopButton, BorderLayout.SOUTH);
        inputPanel.add(inputField, BorderLayout.NORTH);
        inputPanel.add(inputEnterButton, BorderLayout.CENTER);

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

        calculating = false;

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

            for (ActionListener removingListener : inputEnterButton.getActionListeners()) {
                inputEnterButton.removeActionListener(removingListener);
                inputField.removeActionListener(removingListener);
                inputStopButton.removeActionListener(removingListener);
            }
            if (listener != null) {
                inputEnterButton.addActionListener(listener);
                inputField.addActionListener(listener);
                inputStopButton.addActionListener(listener);
            }
        });
        add(pane, BorderLayout.CENTER);
        add(inputPanel, BorderLayout.SOUTH);

        inputEnterButton.addActionListener(evaluateListener);
        inputField.addActionListener(evaluateListener);
        inputStopButton.addActionListener(stopListener);
        historyTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                Point clickPoint = e.getPoint();
                if (e.getClickCount() == 2) {
                    int row = historyTable.rowAtPoint(clickPoint);
                    int column = historyTable.columnAtPoint(clickPoint);
                    String toCopy = historyTable.getValueAt(row, column).toString();
                    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(toCopy), null);
                }
            }
        });
    }

    @Override
    public void setVisible(boolean b) {
        super.setVisible(b);
        if(b) inputField.requestFocusInWindow();
    }
}
