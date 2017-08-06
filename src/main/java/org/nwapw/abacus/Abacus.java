package org.nwapw.abacus;

import org.nwapw.abacus.config.Configuration;
import org.nwapw.abacus.fx.AbacusApplication;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.parsing.LexerTokenizer;
import org.nwapw.abacus.parsing.ShuntingYardParser;
import org.nwapw.abacus.parsing.TreeBuilder;
import org.nwapw.abacus.plugin.NumberImplementation;
import org.nwapw.abacus.plugin.PluginManager;
import org.nwapw.abacus.plugin.StandardPlugin;
import org.nwapw.abacus.tree.NumberReducer;
import org.nwapw.abacus.tree.TreeNode;

import java.util.function.Supplier;

/**
 * The main calculator class. This is responsible
 * for piecing together all of the components, allowing
 * their interaction with each other.
 */
public class Abacus {

    /**
     * The default number implementation to be used if no other one is available / selected.
     */
    public static final NumberImplementation DEFAULT_IMPLEMENTATION = StandardPlugin.IMPLEMENTATION_NAIVE;

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
    private Configuration configuration;
    /**
     * The tree builder used to construct a tree
     * from a string.
     */
    private TreeBuilder treeBuilder;

    /**
     * Creates a new instance of the Abacus calculator.
     */
    public Abacus(Supplier<Configuration> configurationSupplier) {
        pluginManager = new PluginManager(this);
        numberReducer = new NumberReducer(this);
        configuration = configurationSupplier.get();
        LexerTokenizer lexerTokenizer = new LexerTokenizer();
        ShuntingYardParser shuntingYardParser = new ShuntingYardParser(this);
        treeBuilder = new TreeBuilder<>(lexerTokenizer, shuntingYardParser);

        pluginManager.addListener(shuntingYardParser);
        pluginManager.addListener(lexerTokenizer);
    }

    public static void main(String[] args) {
        AbacusApplication.launch(AbacusApplication.class, args);
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
    public Configuration getConfiguration() {
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
        return treeBuilder.fromString(input);
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
        NumberImplementation toInstantiate =
                pluginManager.numberImplementationFor(configuration.getNumberImplementation());
        if (toInstantiate == null) toInstantiate = DEFAULT_IMPLEMENTATION;

        return toInstantiate.instanceForString(numberString);
    }
}
