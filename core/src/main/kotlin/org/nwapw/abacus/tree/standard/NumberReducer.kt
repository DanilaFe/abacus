package org.nwapw.abacus.tree.standard

import org.nwapw.abacus.Abacus
import org.nwapw.abacus.context.EvaluationContext
import org.nwapw.abacus.exception.NumberReducerException
import org.nwapw.abacus.exception.ReductionException
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.tree.Reducer
import org.nwapw.abacus.tree.nodes.*

class NumberReducer(val abacus: Abacus, context: EvaluationContext) : Reducer<NumberInterface> {

    val context = context.mutableSubInstance()

    init {
        this.context.reducer = this
    }

    override fun reduceNode(treeNode: TreeNode, vararg children: Any): NumberInterface {
        val promotionManager = abacus.promotionManager
        return when(treeNode){
            is NumberNode -> {
                context.inheritedNumberImplementation.instanceForString(treeNode.number)
            }
            is VariableNode -> {
                val variable = context.getVariable(treeNode.variable)
                if(variable != null) return variable
                val definition = context.getDefinition(treeNode.variable)
                if(definition != null) return definition.reduce(this)
                throw NumberReducerException("variable is not defined.")
            }
            is NumberUnaryNode -> {
                val child = children[0] as NumberInterface
                context.numberImplementation = abacus.pluginManager.interfaceImplementationFor(child.javaClass)
                abacus.pluginManager.operatorFor(treeNode.operation)
                        .apply(context, child)
            }
            is NumberBinaryNode -> {
                val left = children[0] as NumberInterface
                val right = children[1] as NumberInterface
                val promotionResult = promotionManager.promote(left, right)
                context.numberImplementation = promotionResult.promotedTo
                abacus.pluginManager.operatorFor(treeNode.operation).apply(context, *promotionResult.items)
            }
            is NumberFunctionNode -> {
                val promotionResult = promotionManager
                        .promote(*children.map { it as NumberInterface }.toTypedArray())
                context.numberImplementation = promotionResult.promotedTo
                abacus.pluginManager.functionFor(treeNode.callTo).apply(context, *promotionResult.items)
            }
            is TreeValueUnaryNode -> {
                abacus.pluginManager.treeValueOperatorFor(treeNode.operation)
                        .apply(context, treeNode.applyTo)
            }
            is TreeValueBinaryNode -> {
                abacus.pluginManager.treeValueOperatorFor(treeNode.operation)
                        .apply(context, treeNode.left, treeNode.right)
            }
            is TreeValueFunctionNode -> {
                abacus.pluginManager.treeValueFunctionFor(treeNode.callTo)
                        .apply(context, *treeNode.children.toTypedArray())
            }
            else -> throw ReductionException("unrecognized tree node.")
        }
    }

}