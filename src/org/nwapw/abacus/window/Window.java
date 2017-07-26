package org.nwapw.abacus.window;

import javax.swing.*;
import java.awt.*;

/**
 * The main UI window for the calculator.
 */
public class Window extends JFrame {

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
     * The text area used for all history output.
     */
    private JTextArea historyArea;
    /**
     * The scroll pane for the history area.
     */
    private JScrollPane historyAreaScroll;

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

    public Window() {
        super();


        history = "";
        lastOutput = "";

        setSize(640, 480);

        inputField = new JTextField();
        inputEnterButton = new JButton("Calculate");

        inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputField, BorderLayout.CENTER);
        inputPanel.add(inputEnterButton, BorderLayout.EAST);

        historyArea = new JTextArea(history);
        historyAreaScroll = new JScrollPane(historyArea);
        lastOutputArea = new JTextArea(lastOutput);
        lastOutputArea.setEditable(false);
        lastOutputArea.setText(":)");

        outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());
        outputPanel.add(historyAreaScroll, BorderLayout.CENTER);
        outputPanel.add(lastOutputArea, BorderLayout.SOUTH);

        numberSystemList = new JComboBox<>();

        numberSystemPanel = new JPanel();
        numberSystemPanel.setLayout(new BorderLayout());
        numberSystemPanel.add(new JLabel("Number Type:"), BorderLayout.NORTH);
        numberSystemPanel.add(numberSystemList, BorderLayout.CENTER);

        functionList = new JComboBox<>();
        functionSelectButton = new JButton("Select");

        functionSelectPanel = new JPanel();
        functionSelectPanel.setLayout(new BorderLayout());
        functionSelectPanel.add(new JLabel("Functions:"), BorderLayout.NORTH);
        functionSelectPanel.add(functionList, BorderLayout.CENTER);
        functionSelectPanel.add(functionSelectButton, BorderLayout.SOUTH);

        sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.add(numberSystemPanel, BorderLayout.NORTH);
        sidePanel.add(functionSelectPanel, BorderLayout.SOUTH);

        add(outputPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);
    }
}
