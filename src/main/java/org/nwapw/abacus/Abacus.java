package org.nwapw.abacus;

import org.nwapw.abacus.config.ConfigurationObject;
import org.nwapw.abacus.number.NaiveNumber;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.parsing.LexerTokenizer;
import org.nwapw.abacus.parsing.ShuntingYardParser;
import org.nwapw.abacus.parsing.TreeBuilder;
import org.nwapw.abacus.plugin.ClassFinder;
import org.nwapw.abacus.plugin.PluginManager;
import org.nwapw.abacus.plugin.StandardPlugin;
import org.nwapw.abacus.tree.NumberReducer;
import org.nwapw.abacus.tree.TreeNode;
import org.nwapw.abacus.window.Window;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

/**
 * The main calculator class. This is responsible
 * for piecing together all of the components, allowing
 * their interaction with each other.
 */
public class Abacus {

    /**
     * The default implementation to use for the number representation.
     */
    public static final Class<? extends NumberInterface> DEFAULT_NUMBER = NaiveNumber.class;
    /**
     * The file used for saving and loading configuration.
     */
    public static final File CONFIG_FILE = new File("config.toml");

    /**
     * The plugin manager responsible for
     * loading and unloading plugins,
     * and getting functions from them.
     */
    private PluginManager pluginManager;
    /**
     * The reducer used to evaluate the tree.
     */
    private NumberReducer numberReducer;
    /**
     * The configuration loaded from a file.
     */
    private ConfigurationObject configuration;
    /**
     * The tree builder used to construct a tree
     * from a string.
     */
    private TreeBuilder treeBuilder;
    private Window window;
    public boolean getStop(){
        return window.getStop();
    }
    /**
     * Creates a new instance of the Abacus calculator.
     */
    public Abacus() {
        pluginManager = new PluginManager();
        numberReducer = new NumberReducer(this);
        configuration = new ConfigurationObject(CONFIG_FILE);
        configuration.save(CONFIG_FILE);
        LexerTokenizer lexerTokenizer = new LexerTokenizer();
        ShuntingYardParser shuntingYardParser = new ShuntingYardParser(this);
        treeBuilder = new TreeBuilder<>(lexerTokenizer, shuntingYardParser);

        pluginManager.addListener(lexerTokenizer);
        pluginManager.addListener(shuntingYardParser);
        pluginManager.addInstantiated(new StandardPlugin(pluginManager));
        try {
            ClassFinder.loadJars("plugins")
                    .forEach(plugin -> pluginManager.addClass(plugin));
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        pluginManager.load();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | UnsupportedLookAndFeelException | IllegalAccessException e) {
            e.printStackTrace();
        }

        new Window(new Abacus()).setVisible(true);
    }

    /**
     * Gets the current tree builder.
     *
     * @return the main tree builder in this abacus instance.
     */
    public TreeBuilder getTreeBuilder() {
        return treeBuilder;
    }

    /**
     * Gets the current plugin manager,
     *
     * @return the plugin manager in this abacus instance.
     */
    public PluginManager getPluginManager() {
        return pluginManager;
    }

    /**
     * Get the reducer that is responsible for transforming
     * an expression into a number.
     *
     * @return the number reducer in this abacus instance.
     */
    public NumberReducer getNumberReducer() {
        return numberReducer;
    }

    /**
     * Gets the configuration object associated with this instance.
     *
     * @return the configuration object.
     */
    public ConfigurationObject getConfiguration() {
        return configuration;
    }

    /**
     * Parses a string into a tree structure using the main
     * tree builder.
     *
     * @param input the input string to parse
     * @return the resulting tree, null if the tree builder or the produced tree are null.
     */
    public TreeNode parseString(String input) {
        return treeBuilder.fromString(input,this);
    }

    /**
     * Evaluates the given tree using the main
     * number reducer.
     *
     * @param tree the tree to reduce, must not be null.
     * @return the resulting number, or null of the reduction failed.
     */
    public NumberInterface evaluateTree(TreeNode tree) {
        return tree.reduce(numberReducer);
    }

    /**
     * Creates a number from a string.
     *
     * @param numberString the string to create the number from.
     * @return the resulting number.
     */
    public NumberInterface numberFromString(String numberString) {
        Class<? extends NumberInterface> toInstantiate =
                pluginManager.numberFor(configuration.getNumberImplementation());
        if (toInstantiate == null) toInstantiate = DEFAULT_NUMBER;

        try {
            return toInstantiate.getConstructor(String.class).newInstance(numberString);
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void setWindow(Window window) {
        this.window = window;
    }
}
