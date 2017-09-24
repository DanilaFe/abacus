package org.nwapw.abacus.plugin.standard

import org.nwapw.abacus.context.MutableEvaluationContext
import org.nwapw.abacus.function.OperatorAssociativity
import org.nwapw.abacus.function.OperatorType
import org.nwapw.abacus.function.TreeValueOperator
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.tree.TreeNode
import org.nwapw.abacus.tree.VariableNode

/**
 * The definition operator.
 *
 * This is a standard operator that creates a definition - something that doesn't capture variable values
 * when it's created, but rather the variables themselves, and changes when the variables it uses change.
 */
class OperatorDefine: TreeValueOperator(OperatorAssociativity.LEFT, OperatorType.BINARY_INFIX, 0) {

    override fun matchesParams(context: MutableEvaluationContext, params: Array<out TreeNode>) =
            params.size == 2 && params[0] is VariableNode

    override fun applyInternal(context: MutableEvaluationContext, params: Array<out TreeNode>): NumberInterface {
        val assignTo = (params[0] as VariableNode).variable
        context.setDefinition(assignTo, params[1])
        return params[1].reduce(context.inheritedReducer)
    }

}