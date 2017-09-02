package org.nwapw.abacus;

import org.nwapw.abacus.config.Configuration;
import org.nwapw.abacus.number.NaiveNumber;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.number.PreciseNumber;
import org.nwapw.abacus.number.PromotionManager;
import org.nwapw.abacus.parsing.LexerTokenizer;
import org.nwapw.abacus.parsing.ShuntingYardParser;
import org.nwapw.abacus.parsing.TreeBuilder;
import org.nwapw.abacus.plugin.NumberImplementation;
import org.nwapw.abacus.plugin.PluginManager;
import org.nwapw.abacus.plugin.StandardPlugin;
import org.nwapw.abacus.tree.NumberReducer;
import org.nwapw.abacus.tree.TreeNode;

import java.math.BigDecimal;

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
     * Promotion manager responsible for the promotion system.
     */
    private PromotionManager promotionManager;

    /**
     * Creates a new instance of the Abacus calculator.
     *
     * @param configuration the configuration object for this Abacus instance.
     */
    public Abacus(Configuration configuration) {
        pluginManager = new PluginManager(this);
        numberReducer = new NumberReducer(this);
        this.configuration = new Configuration(configuration);
        LexerTokenizer lexerTokenizer = new LexerTokenizer();
        ShuntingYardParser shuntingYardParser = new ShuntingYardParser();
        treeBuilder = new TreeBuilder<>(lexerTokenizer, shuntingYardParser);
        promotionManager = new PromotionManager(this);

        pluginManager.addListener(shuntingYardParser);
        pluginManager.addListener(lexerTokenizer);
    }

    /**
     * Gets the promotion manager.
     * @return the promotion manager.
     */
    public PromotionManager getPromotionManager() {
        return promotionManager;
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
     * Gets the number implementation.
     * @return the number implementation to use for creating numbers.
     */
    public NumberImplementation getNumberImplementation(){
        NumberImplementation selectedImplementation =
                pluginManager.numberImplementationFor(configuration.getNumberImplementation());
        if(selectedImplementation != null) return selectedImplementation;
        return DEFAULT_IMPLEMENTATION;
    }

}
