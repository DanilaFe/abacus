package org.nwapw.abacus.context

import org.nwapw.abacus.Abacus
import org.nwapw.abacus.exception.NumberReducerException
import org.nwapw.abacus.exception.ReductionException
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.plugin.NumberImplementation
import org.nwapw.abacus.tree.nodes.*

/**
 * A reduction context that is mutable.
 *
 * @param parent the parent of this context.
 * @param numberImplementation the number implementation used in this context.
 * @param abacus the abacus instance used.
 */
class MutableEvaluationContext(parent: EvaluationContext? = null,
                               numberImplementation: NumberImplementation? = null,
                               abacus: Abacus? = null) :
        PluginEvaluationContext(parent, numberImplementation, abacus) {

    override var numberImplementation: NumberImplementation? = super.numberImplementation
    override var abacus: Abacus? = super.abacus

    /**
     * Writes data stored in the [other] context over data stored in this one.
     * @param other the context from which to copy data.
     */
    fun apply(other: EvaluationContext) {
        if(other.numberImplementation != null) numberImplementation = other.numberImplementation
        for(name in other.variables) {
            setVariable(name, other.getVariable(name) ?: continue)
        }
        for(name in other.definitions) {
            setDefinition(name, other.getDefinition(name) ?: continue)
        }
    }

    override fun reduceNode(treeNode: TreeNode, vararg children: Any): NumberInterface {
        val oldNumberImplementation = numberImplementation
        val abacus = inheritedAbacus
        val promotionManager = abacus.promotionManager
        val toReturn = when(treeNode){
            is NumberNode -> {
                inheritedNumberImplementation.instanceForString(treeNode.number)
            }
            is VariableNode -> {
                val variable = getVariable(treeNode.variable)
                if(variable != null) return variable
                val definition = getDefinition(treeNode.variable)
                if(definition != null) return definition.reduce(this)
                throw NumberReducerException("variable is not defined.")
            }
            is NumberUnaryNode -> {
                val child = children[0] as NumberInterface
                numberImplementation = abacus.pluginManager.interfaceImplementationFor(child.javaClass)
                abacus.pluginManager.operatorFor(treeNode.operation)
                        .apply(this, child)
            }
            is NumberBinaryNode -> {
                val left = children[0] as NumberInterface
                val right = children[1] as NumberInterface
                val promotionResult = promotionManager.promote(left, right)
                numberImplementation = promotionResult.promotedTo
                abacus.pluginManager.operatorFor(treeNode.operation).apply(this, *promotionResult.items)
            }
            is NumberFunctionNode -> {
                val promotionResult = promotionManager
                        .promote(*children.map { it as NumberInterface }.toTypedArray())
                numberImplementation = promotionResult.promotedTo
                abacus.pluginManager.functionFor(treeNode.callTo).apply(this, *promotionResult.items)
            }
            is TreeValueUnaryNode -> {
                abacus.pluginManager.treeValueOperatorFor(treeNode.operation)
                        .apply(this, treeNode.applyTo)
            }
            is TreeValueBinaryNode -> {
                abacus.pluginManager.treeValueOperatorFor(treeNode.operation)
                        .apply(this, treeNode.left, treeNode.right)
            }
            is TreeValueFunctionNode -> {
                abacus.pluginManager.treeValueFunctionFor(treeNode.callTo)
                        .apply(this, *treeNode.children.toTypedArray())
            }
            else -> throw ReductionException("unrecognized tree node.")
        }
        numberImplementation = oldNumberImplementation
        return toReturn
    }

}