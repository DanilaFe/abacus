package org.nwapw.abacus.window;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private String history;
    private String lastOutput;

    private JPanel outputPanel;
    private JTextArea lastOutputArea;
    private JTextArea historyArea;
    private JScrollPane historyAreaScroll;

    private JPanel inputPanel;
    private JTextField inputField;
    private JButton inputEnterButton;

    private JPanel sidePanel;
    private JPanel numberSystemPanel;
    private JComboBox<String> numberSystemList;
    private JButton functionSelectButton;
    private JPanel functionSelectPanel;
    private JComboBox<String> functionList;

    public Window() {
        super();


        history = "";
        lastOutput = "";

        setSize(640, 480);

        inputField = new JTextField();
        inputEnterButton = new JButton("Calculate");

        inputPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        inputPanel.add(inputField);
        inputPanel.add(inputEnterButton, BorderLayout.LINE_END);

        historyArea = new JTextArea(history);
        historyAreaScroll = new JScrollPane(historyArea);
        lastOutputArea = new JTextArea(lastOutput);
        lastOutputArea.setEditable(false);
        lastOutputArea.setText(":)");

        outputPanel = new JPanel();
        outputPanel.setLayout(new BorderLayout());
        outputPanel.add(historyAreaScroll);
        outputPanel.add(lastOutputArea, BorderLayout.PAGE_END);

        numberSystemList = new JComboBox<>();

        numberSystemPanel = new JPanel();
        numberSystemPanel.setLayout(new BorderLayout());
        numberSystemPanel.add(new JLabel("Number Type:"));
        numberSystemPanel.add(numberSystemList, BorderLayout.PAGE_END);

        functionList = new JComboBox<>();
        functionSelectButton = new JButton("Select");

        functionSelectPanel = new JPanel();
        functionSelectPanel.setLayout(new BorderLayout());
        functionSelectPanel.add(new JLabel("Functions:"));
        functionSelectPanel.add(functionList, BorderLayout.LINE_END);
        functionSelectPanel.add(functionSelectButton, BorderLayout.LINE_END);

        sidePanel = new JPanel();
        sidePanel.setLayout(new BorderLayout());
        sidePanel.add(numberSystemPanel);
        sidePanel.add(functionSelectPanel, BorderLayout.PAGE_END);

        add(outputPanel, BorderLayout.CENTER);
        add(sidePanel, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);
    }
}
