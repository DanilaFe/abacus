package org.nwapw.abacus.tree

import org.nwapw.abacus.Abacus
import org.nwapw.abacus.context.EvaluationContext
import org.nwapw.abacus.exception.EvaluationException
import org.nwapw.abacus.number.NumberInterface

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
                        ?: throw EvaluationException("no number implementation selected.")
            }
            is VariableNode -> {
                val variable = context.getVariable(treeNode.variable)
                if(variable != null) return variable
                val definition = context.getDefinition(treeNode.variable)
                if(definition != null) return definition.reduce(this)
                throw EvaluationException("variable is not defined.")
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
            is FunctionNode -> {
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
            else -> throw EvaluationException("unrecognized tree node.")
        }
    }

}