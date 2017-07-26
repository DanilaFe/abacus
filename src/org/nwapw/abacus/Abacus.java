package org.nwapw.abacus;

import org.nwapw.abacus.plugin.PluginManager;
import org.nwapw.abacus.plugin.StandardPlugin;
import org.nwapw.abacus.window.Window;

import javax.swing.*;

public class Abacus {

    private Window mainUi;
    private PluginManager manager;

    public Abacus(){
        init();
    }

    private void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }
        manager = new PluginManager();
        manager.addInstantiated(new StandardPlugin(manager));
        mainUi = new Window();
        mainUi.setVisible(true);
    }

    public static void main(String[] args){
        new Abacus();
    }

}
