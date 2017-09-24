package org.nwapw.abacus.plugin.standard

import org.nwapw.abacus.context.MutableEvaluationContext
import org.nwapw.abacus.function.OperatorAssociativity
import org.nwapw.abacus.function.OperatorType
import org.nwapw.abacus.function.TreeValueOperator
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.tree.TreeNode
import org.nwapw.abacus.tree.VariableNode

/**
 * The set operator.
 *
 * This is a standard operator that assigns a value to a variable name.
 */
class OperatorSet: TreeValueOperator(OperatorAssociativity.LEFT, OperatorType.BINARY_INFIX, 0) {

    override fun matchesParams(context: MutableEvaluationContext, params: Array<out TreeNode>) =
            params.size == 2 && params[0] is VariableNode

    override fun applyInternal(context: MutableEvaluationContext, params: Array<out TreeNode>): NumberInterface {
        val assignTo = (params[0] as VariableNode).variable
        val value = params[1].reduce(context.inheritedReducer)
        context.setVariable(assignTo, value)
        return value
    }

}