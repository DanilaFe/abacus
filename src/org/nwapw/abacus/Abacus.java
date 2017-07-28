package org.nwapw.abacus;

import org.nwapw.abacus.plugin.PluginManager;
//import org.nwapw.abacus.plugin.StandardPlugin;
import org.nwapw.abacus.window.Window;
import org.nwapw.abacus.plugin.ClassFinderV2;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

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
        ArrayList<String> names = new ArrayList();
        try {

            ClassFinderV2 classFinder = new ClassFinderV2();
            File pluginFile = new File("C:\\Users\\galbraithja\\Desktop\\.git\\abacus\\src\\org\\nwapw\\abacus\\plugin");
            for(File classes:pluginFile.listFiles()){
                if(classes.getName().endsWith(".jar")){
                    names.addAll(classFinder.addJar("C:\\Users\\galbraithja\\Desktop\\.git\\abacus\\src\\org\\nwapw\\abacus\\plugin\\Standard.jar"));
                }
            }
            for(String name:names){
                System.out.println(name);
            }
            ArrayList<Class> classes = classFinder.getClasses();
            for(Class classGet:classes){
                manager.addClass(classGet);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        mainUi = new Window(manager);
        mainUi.setVisible(true);
        manager.load();

    }

    public static void main(String[] args){
        new Abacus();
    }

}
