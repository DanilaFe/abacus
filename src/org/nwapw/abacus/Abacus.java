package org.nwapw.abacus;

import org.nwapw.abacus.window.Window;

import javax.swing.*;

public class Abacus {

    private Window mainUi;

    public Abacus(){
        init();
    }

    private void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
        mainUi = new Window();
        mainUi.setVisible(true);
    }

    public static void main(String[] args){
        new Abacus();
    }

}
