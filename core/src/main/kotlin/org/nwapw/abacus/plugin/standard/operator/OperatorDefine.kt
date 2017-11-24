package org.nwapw.abacus.plugin.standard.operator

import org.nwapw.abacus.context.PluginEvaluationContext
import org.nwapw.abacus.function.OperatorAssociativity
import org.nwapw.abacus.function.OperatorType
import org.nwapw.abacus.function.interfaces.TreeValueOperator
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.tree.nodes.TreeNode
import org.nwapw.abacus.tree.nodes.VariableNode

/**
 * The definition operator.
 *
 * This is a standard operator that creates a definition - something that doesn't capture variable values
 * when it's created, but rather the variables themselves, and changes when the variables it uses change.
 */
class OperatorDefine: TreeValueOperator(OperatorAssociativity.LEFT, OperatorType.BINARY_INFIX, 0) {

    override fun matchesParams(context: PluginEvaluationContext, params: Array<out TreeNode>) =
            params.size == 2 && params[0] is VariableNode

    override fun applyInternal(context: PluginEvaluationContext, params: Array<out TreeNode>): NumberInterface {
        val assignTo = (params[0] as VariableNode).variable
        context.setDefinition(assignTo, params[1])
        return params[1].reduce(context)
    }

}