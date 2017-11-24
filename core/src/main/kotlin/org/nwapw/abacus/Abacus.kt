package org.nwapw.abacus

import org.nwapw.abacus.config.Configuration
import org.nwapw.abacus.context.EvaluationContext
import org.nwapw.abacus.context.MutableEvaluationContext
import org.nwapw.abacus.number.promotion.PromotionManager
import org.nwapw.abacus.parsing.TreeBuilder
import org.nwapw.abacus.parsing.standard.LexerTokenizer
import org.nwapw.abacus.parsing.standard.ShuntingYardParser
import org.nwapw.abacus.plugin.PluginManager
import org.nwapw.abacus.plugin.standard.StandardPlugin
import org.nwapw.abacus.tree.nodes.TreeNode

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
     * The tree builder that handles the conversion of strings into trees.
     */
    val treeBuilder = TreeBuilder(tokenizer, parser)
    /**
     * The promotion manager used to convert between number implementations.
     */
    val promotionManager = PromotionManager(this)

    /**
     * The hidden, mutable implementation of the context.
     */
    private val mutableContext = MutableEvaluationContext(numberImplementation = StandardPlugin.IMPLEMENTATION_NAIVE, abacus = this)
    /**
     * The base context from which calculations are started.
     */
    val context: EvaluationContext
        get() = mutableContext

    init {
        pluginManager.addListener(tokenizer)
        pluginManager.addListener(parser)
        pluginManager.addListener(promotionManager)
    }

    /**
     * Reloads the Abacus core.
     */
    fun reload(){
        with(mutableContext){
            clearVariables()
            clearDefinitions()
        }
        pluginManager.reload()
        mutableContext.numberImplementation = pluginManager.numberImplementationFor(configuration.numberImplementation)
                ?: StandardPlugin.IMPLEMENTATION_NAIVE
    }
    /**
     * Merges the current context with the provided one, updating
     * variables and the like.
     * @param context the context to apply.
     */
    fun applyToContext(context: EvaluationContext){
        mutableContext.apply(context)
    }
    /**
     * Parses a string into a tree structure using the main
     * tree builder.
     *
     * @param input the input string to parse
     * @return the resulting tree, null if the tree builder or the produced tree are null.
     */
    fun parseString(input: String): TreeNode = treeBuilder.fromString(input)
    /**
     * Evaluates the given tree.
     *
     * @param tree the tree to reduce, must not be null.
     * @return the evaluation result.
     */
    fun evaluateTree(tree: TreeNode): EvaluationResult {
        return evaluateTreeWithContext(tree, context.mutableSubInstance())
    }
    /**
     * Evaluates the given tree using a different context than
     * the default one.
     *
     * @param tree the tree to reduce, must not be null.
     * @param context the context to use for the evaluation.
     * @return the evaluation result.
     */
    fun evaluateTreeWithContext(tree: TreeNode, context: MutableEvaluationContext): EvaluationResult {
        val evaluationValue = tree.reduce(context)
        return EvaluationResult(evaluationValue, context)
    }

}