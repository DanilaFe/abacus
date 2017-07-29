package org.nwapw.abacus;

import org.nwapw.abacus.config.ConfigurationObject;
import org.nwapw.abacus.function.Operator;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.plugin.ClassFinder;
import org.nwapw.abacus.plugin.PluginListener;
import org.nwapw.abacus.plugin.PluginManager;
import org.nwapw.abacus.plugin.StandardPlugin;
import org.nwapw.abacus.tree.NumberReducer;
import org.nwapw.abacus.tree.TreeBuilder;
import org.nwapw.abacus.tree.TreeNode;
import org.nwapw.abacus.window.Window;

import javax.swing.*;
import java.io.File;
import java.io.IOException;

/**
 * The main calculator class. This is responsible
 * for piecing together all of the components, allowing
 * their interaction with each other.
 */
public class Abacus implements PluginListener {

    /**
     * The file used for saving and loading configuration.
     */
    public static final File CONFIG_FILE = new File("config.yml");

    /**
     * The main Abacus UI.
     */
    private Window mainUi;
    /**
     * The plugin manager responsible for
     * loading and unloading plugins,
     * and getting functions from them.
     */
    private PluginManager pluginManager;
    /**
     * Tree builder built from plugin manager,
     * used to construct parse trees.
     */
    private TreeBuilder treeBuilder;
    /**
     * The reducer used to evaluate the tree.
     */
    private NumberReducer numberReducer;
    /**
     * The configuration loaded from a file.
     */
    private ConfigurationObject configuration;

    /**
     * Creates a new instance of the Abacus calculator.
     */
    public Abacus(){
        pluginManager = new PluginManager(this);
        mainUi = new Window(this);
        numberReducer = new NumberReducer(this);
        configuration = new ConfigurationObject(CONFIG_FILE);
        configuration.save(CONFIG_FILE);

        pluginManager.addListener(this);
        pluginManager.addInstantiated(new StandardPlugin(pluginManager));
        try {
            ClassFinder.loadJars("plugins")
                    .forEach(plugin -> pluginManager.addClass(plugin));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        pluginManager.load();

        mainUi.setVisible(true);
    }

    /**
     * Gets the current tree builder.
     * @return the main tree builder in this abacus instance.
     */
    public TreeBuilder getTreeBuilder() {
        return treeBuilder;
    }

    /**
     * Gets the current plugin manager,
     * @return the plugin manager in this abacus instance.
     */
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    /**
     * Gets the current UI.
     * @return the UI window in this abacus instance.
     */
    public Window getMainUi() {
        return mainUi;
    }

    /**
     * Get the reducer that is responsible for transforming
     * an expression into a number.
     * @return the number reducer in this abacus instance.
     */
    public NumberReducer getNumberReducer() {
        return numberReducer;
    }

    /**
     * Gets the configuration object associated with this instance.
     * @return the configuration object.
     */
    public ConfigurationObject getConfiguration() {
        return configuration;
    }

    /**
     * Parses a string into a tree structure using the main
     * tree builder.
     * @param input the input string to parse
     * @return the resulting tree, null if the tree builder or the produced tree are null.
     */
    public TreeNode parseString(String input){
        if(treeBuilder == null) return null;
        return treeBuilder.fromString(input);
    }

    /**
     * Evaluates the given tree using the main
     * number reducer.
     * @param tree the tree to reduce, must not be null.
     * @return the resulting number, or null of the reduction failed.
     */
    public NumberInterface evaluateTree(TreeNode tree){
        return tree.reduce(numberReducer);
    }

    @Override
    public void onLoad(PluginManager manager) {
        treeBuilder = new TreeBuilder();
        for(String function : manager.getAllFunctions()){
            treeBuilder.registerFunction(function);
        }
        for(String operator : manager.getAllOperators()){
            Operator operatorObject = manager.operatorFor(operator);
            treeBuilder.registerOperator(operator,
                    operatorObject.getAssociativity(),
                    operatorObject.getType(),
                    operatorObject.getPrecedence());
        }
    }

    @Override
    public void onUnload(PluginManager manager) {
        treeBuilder = null;
    }

    public static void main(String[] args){
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        new Abacus();
    }
}
