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
}
