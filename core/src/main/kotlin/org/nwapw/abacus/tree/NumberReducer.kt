package org.nwapw.abacus.tree

import org.nwapw.abacus.Abacus
import org.nwapw.abacus.context.MutableReductionContext
import org.nwapw.abacus.function.NumberFunction
import org.nwapw.abacus.number.NumberInterface

class NumberReducer(val abacus: Abacus, val context: MutableReductionContext) : Reducer<NumberInterface> {

    override fun reduceNode(treeNode: TreeNode, vararg children: Any): NumberInterface? {
        val promotionManager = abacus.promotionManager
        return when(treeNode){
            is NumberNode -> {
                context.inheritedNumberImplementation?.instanceForString(treeNode.number)
            }
            is VariableNode -> {
                val variable = context.getVariable(treeNode.variable)
                if(variable != null) return variable
                val definition = context.getDefinition(treeNode.variable)
                if(definition != null) return definition.reduce(this)
                null
            }
            is NumberUnaryNode -> {
                val child = children[0] as NumberInterface
                abacus.pluginManager.operatorFor(treeNode.operation)
                        .apply(abacus.pluginManager.interfaceImplementationFor(child.javaClass), child)
            }
            is NumberBinaryNode -> {
                val left = children[0] as NumberInterface
                val right = children[1] as NumberInterface
                val promotionResult = promotionManager.promote(left, right) ?: return null
                abacus.pluginManager.operatorFor(treeNode.operation).apply(promotionResult.promotedTo, *promotionResult.items)
            }
            is FunctionNode -> {
                val promotionResult = promotionManager
                        .promote(*children.map { it as NumberInterface }.toTypedArray()) ?: return null
                abacus.pluginManager.functionFor(treeNode.callTo).apply(promotionResult.promotedTo, *promotionResult.items)
            }
            is TreeValueUnaryNode -> {
                abacus.pluginManager.treeValueOperatorFor(treeNode.operation)
                        .applyWithReducer(context.inheritedNumberImplementation!!, this, children[0] as TreeNode)
            }
            is TreeValueBinaryNode -> {
                abacus.pluginManager.treeValueOperatorFor(treeNode.operation)
                        .applyWithReducer(context.inheritedNumberImplementation!!, this,
                                children[0] as TreeNode, children[1] as TreeNode)
            }
            is TreeValueFunctionNode -> {
                abacus.pluginManager.treeValueFunctionFor(treeNode.callTo)
                        .applyWithReducer(context.inheritedNumberImplementation!!, this,
                                *children.map { it as TreeNode }.toTypedArray())
            }
            else -> null
        }
    }

}