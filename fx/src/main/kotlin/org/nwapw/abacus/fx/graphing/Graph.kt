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

class Graph(val abacus: Abacus,
            expression: String, pointExpression: String,
            var domain: ClosedRange<NumberInterface>, var range: ClosedRange<NumberInterface>,
            var inputVariable: String = "x", var pointInputVariable: String = "n") {

    private var expressionTree: TreeNode? = null
    private var pointExpressionTree: TreeNode? = null

    var expression: String = ""
        set(value) {
            expressionTree = abacus.parseString(value)
            field = value
        }
    var pointExpression: String = ""
        set(value) {
            pointExpressionTree = abacus.parseString(value)
            field = value
        }

    init {
        this.expression = expression
        this.pointExpression = pointExpression
    }

    fun <T> evaluateWith(tree: TreeNode, values: List<T>,
                         contextModifier: MutableEvaluationContext.(T) -> Unit) = runBlocking {
        values.map {
            val context = abacus.context.mutableSubInstance()
            context.contextModifier(it)
            async(CommonPool) { abacus.evaluateTreeWithContext(tree, context) }
        }.map { it.await() }
    }

    fun <T> List<T>.evaluateWith(tree: TreeNode,
                                 contextModifier: MutableEvaluationContext.(T) -> Unit ) =
            evaluateWith(tree, this, contextModifier)

    fun generateInputs(): List<NumberInterface> =
            generateSequence(1) {
                it + 1
            }.map {
                val context = abacus.context.mutableSubInstance()
                context.setVariable(pointInputVariable,
                        context.inheritedNumberImplementation!!.instanceForString(it.toString()))
                abacus.evaluateTreeWithContext(pointExpressionTree!!, context).value
            }.takeWhile { it in domain }.toList()

    fun generateOutputs(inputs: List<NumberInterface>): List<NumberInterface> =
            inputs.evaluateWith(expressionTree!!) {
                setVariable(inputVariable, it)
            }.map { it.value }
}
