package org.nwapw.abacus.window;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {
    private String history;
    private String lastOutput;
    public Window(){
        super();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        
        //JFrame super= new JFrame();
        //ImageIcon check = new ImageIcon(".\\Window Images\\Untitled.png");
        String check = "Enter";
        JButton checkButton = new JButton(check);
        JTextField inputBox = new JTextField();
        history="";
        JTextArea historyBox = new JTextArea(history);
        JScrollPane scrollHistoryBox = new JScrollPane(historyBox);
        JTextArea outputBox = new JTextArea(lastOutput);
        JPanel output=new JPanel();
        output.setLayout(new BorderLayout());
        output.add(scrollHistoryBox);
        output.add(outputBox, BorderLayout.PAGE_END);
        JPanel input = new JPanel();
        input.setLayout(new BorderLayout());
        input.add(inputBox);
        input.add(checkButton, BorderLayout.LINE_END);
        super.add(output);
        JPanel custom = new JPanel();
        JPanel numCustom = new JPanel();
        JPanel funCustom = new JPanel();
        custom.setLayout(new BorderLayout());
        numCustom.setLayout(new BorderLayout());
        funCustom.setLayout(new BorderLayout());
        JTextArea numLabel = new JTextArea("Number Type:");
        JTextArea funLabel = new JTextArea("Functions:");
        JComboBox<String> numList = new JComboBox();
        JComboBox<String> funList = new JComboBox();
        JButton funCheckButton = new JButton(check);
        numCustom.add(numLabel);
        numCustom.add(numList, BorderLayout.PAGE_END);
        funCustom.add(funList);
        funCustom.add(funCheckButton, BorderLayout.LINE_END);
        funCustom.add(funLabel, BorderLayout.PAGE_START);
        custom.add(numCustom);
        custom.add(funCustom, BorderLayout.PAGE_END);
        super.add(custom, BorderLayout.LINE_END);
        super.add(input, BorderLayout.PAGE_END);
        super.setVisible(true);
    }
}
