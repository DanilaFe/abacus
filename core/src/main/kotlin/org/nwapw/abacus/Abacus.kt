package org.nwapw.abacus

import org.nwapw.abacus.config.Configuration
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.number.PromotionManager
import org.nwapw.abacus.parsing.LexerTokenizer
import org.nwapw.abacus.parsing.ShuntingYardParser
import org.nwapw.abacus.parsing.TreeBuilder
import org.nwapw.abacus.plugin.NumberImplementation
import org.nwapw.abacus.plugin.PluginManager
import org.nwapw.abacus.plugin.StandardPlugin
import org.nwapw.abacus.tree.NumberReducer
import org.nwapw.abacus.tree.TreeNode
import org.nwapw.abacus.variables.VariableDatabase

/**
 * Core class to handle all mathematics.
 *
 * The main calculator class. This is responsible
 * for piecing together all of the components, allowing
 * their interaction with each other.
 *
 * @property configuration the configuration to use.
 */
class Abacus(val configuration: Configuration) {

    /**
     * The tokenizer used to convert strings into tokens.
     */
    private val tokenizer = LexerTokenizer()
    /**
     * Parser the parser used to convert tokens into trees.
     */
    private val parser = ShuntingYardParser()
    /**
     * The plugin manager used to handle loading and unloading plugins.
     */
    val pluginManager = PluginManager(this)
    /**
     * The reducer used to turn trees into a single number.
     */
    val numberReducer = NumberReducer(this)
    /**
     * The tree builder that handles the conversion of strings into trees.
     */
    val treeBuilder = TreeBuilder(tokenizer, parser)
    /**
     * The promotion manager used to convert between number implementations.
     */
    val promotionManager = PromotionManager(this)
    /**
     * The number implementation used by default.
     */
    val numberImplementation: NumberImplementation
        get() {
            val selectedImplementation =
                    pluginManager.numberImplementationFor(configuration.numberImplementation)
            if (selectedImplementation != null) return selectedImplementation
            return StandardPlugin.IMPLEMENTATION_NAIVE
        }

    init {
        pluginManager.addListener(tokenizer)
        pluginManager.addListener(parser)
        pluginManager.addListener(promotionManager)
    }

    /**
     * Parses a string into a tree structure using the main
     * tree builder.
     *
     * @param input the input string to parse
     * @return the resulting tree, null if the tree builder or the produced tree are null.
     */
    fun parseString(input: String): TreeNode? = treeBuilder.fromString(input)
    /**
     * Evaluates the given tree using the main
     * number reducer.
     *
     * @param tree the tree to reduce, must not be null.
     * @return the resulting number, or null of the reduction failed.
     */
    fun evaluateTree(tree: TreeNode): NumberInterface? = tree.reduce(numberReducer)

}