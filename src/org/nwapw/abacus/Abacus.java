package org.nwapw.abacus;

import org.nwapw.abacus.plugin.PluginManager;
//import org.nwapw.abacus.plugin.StandardPlugin;
import org.nwapw.abacus.window.Window;
import org.nwapw.abacus.plugin.ClassFinder;

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
        ArrayList<String> names = new ArrayList<>();
        try {

            ClassFinder classFinder = new ClassFinder();
            File pluginFile = new File("plugins");
            for(File classes:pluginFile.listFiles()){
                if(classes.getName().endsWith(".jar")){
                    names.addAll(classFinder.addJar(classes));
                }
            }
            for(String name:names){
                System.out.println(name);
            }
            ArrayList<Class> classes = classFinder.getClasses();
            for(Class classGet:classes){
                manager.addClass(classGet);
            }

        } catch (IOException | ClassNotFoundException e) {
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
