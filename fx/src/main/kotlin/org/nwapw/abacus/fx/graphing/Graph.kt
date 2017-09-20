package org.nwapw.abacus.fx.graphing

import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.runBlocking
import org.nwapw.abacus.Abacus
import org.nwapw.abacus.config.Configuration
import org.nwapw.abacus.context.EvaluationContext
import org.nwapw.abacus.context.MutableEvaluationContext
import org.nwapw.abacus.number.NaiveNumber
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.plugin.StandardPlugin
import org.nwapw.abacus.tree.TreeNode

/**
 * A class that holds information about a graph.
 *
 * Graph that uses an instance of [Abacus] to generate graphs of an
 * [expression]. Continuous graphing on large expressions is expensive,
 * and therefore the method of graphing is discrete. The graph class
 * uses the [pointExpression] to generate inputs until an input is outside
 * the function's domain. In both the expressions, the [inputVariable] and
 * [pointInputVariable] are used, respectively, in order to either pass in
 * the x-coordinate or the number of the input value being generated.
 *
 * @property abacus the abacus instance to use to evaluate expressions.
 * @property domain the domain used in computation.
 * @property range the range used for displaying the output.
 * @property inputVariable the variable which is substituted for the input x-coordinate.
 * @property pointInputVariable the variable which is substituted for the number of the input point being generated.
 */
class Graph(val abacus: Abacus,
            var domain: ClosedRange<NumberInterface>, var range: ClosedRange<NumberInterface>,
            var inputVariable: String = "x", var pointInputVariable: String = "n") {

    /**
     * Property used for storing the parsed version of the [expression]
     */
    private var expressionTree: TreeNode? = null
    /**
     * Property used for storing the parsed version of the [pointExpression]
     */
    private var pointExpressionTree: TreeNode? = null

    /**
     * The expression being graphed.
     */
    var expression: String? = null
        set(value) {
            field = value
            expressionTree = abacus.parseString(value ?: return)
        }
    /**
     * The expression being used to generate points.
     */
    var pointExpression: String? = null
        set(value) {
            field = value
            pointExpressionTree = abacus.parseString(value ?: return)
        }

    /**
     * Evaluates a parsed expression [tree] with the given input [values] of indeterminate type.
     * This is is an asynchronous operation using coroutines, and for every input
     * value a new sub-context is created. The [contextModifier] function is executed
     * on each new sub-context, and is expected to use the input value to modify the context
     * state so that the evaluation of the expression produces a correct result.
     *
     * @param T the type of input values.
     * @param tree the tree to evaluate.
     * @param values the values to plug into the expression.
     * @param contextModifier the function that plugs values into the context.
     * @return the list of outputs.
     */
    fun <T> evaluateWith(tree: TreeNode, values: List<T>,
                         contextModifier: MutableEvaluationContext.(T) -> Unit) = runBlocking {
        values.map {
            val context = abacus.context.mutableSubInstance()
            context.contextModifier(it)
            async(CommonPool) { abacus.evaluateTreeWithContext(tree, context) }
        }.map { it.await() }
    }

    /**
     * Extension function that calls [evaluateWith] with the current list.
     * @param T the type of the values in the list.
     * @param tree the tree node to evaluate.
     * @param contextModifier the function that plugs values into the context.
     * @return the list of outputs.
     */
    fun <T> List<T>.evaluateWith(tree: TreeNode,
                                 contextModifier: MutableEvaluationContext.(T) -> Unit ) =
            evaluateWith(tree, this, contextModifier)

    /**
     * Uses the [pointExpression] and [pointInputVariable] to generate
     * a set of points as inputs for the actual expression. These points are generated
     * as long as they are within the [domain].
     * @return the list of generated input values.
     */
    fun generateInputs(): List<NumberInterface> =
            generateSequence(1) {
                it + 1
            }.map {
                val context = abacus.context.mutableSubInstance()
                context.setVariable(pointInputVariable,
                        context.inheritedNumberImplementation!!.instanceForString(it.toString()))
                abacus.evaluateTreeWithContext(pointExpressionTree!!, context).value
            }.takeWhile { it in domain }.toList()

    /**
     * Uses the [expression] and [inputVariable] to generate
     * a set of outputs from the given set of [inputs].
     * @return the list of generated points.
     */
    fun generateOutputs(inputs: List<NumberInterface>): List<Pair<NumberInterface, NumberInterface>> =
            inputs.evaluateWith(expressionTree!!) {
                setVariable(inputVariable, it)
            }.mapIndexed { index, (value) -> inputs[index] to value }
}
