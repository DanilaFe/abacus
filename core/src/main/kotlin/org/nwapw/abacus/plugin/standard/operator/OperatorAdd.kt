package org.nwapw.abacus.plugin.standard.operator

import org.nwapw.abacus.context.PluginEvaluationContext
import org.nwapw.abacus.function.OperatorAssociativity
import org.nwapw.abacus.function.OperatorType
import org.nwapw.abacus.function.interfaces.NumberOperator
import org.nwapw.abacus.number.NumberInterface

/**
 * The addition operator.
 *
 * This is a standard operator that simply performs addition.
 */
class OperatorAdd: NumberOperator(OperatorAssociativity.LEFT, OperatorType.BINARY_INFIX, 0) {

    override fun matchesParams(context: PluginEvaluationContext, params: Array<out NumberInterface>) =
            params.size == 2
    override fun applyInternal(context: PluginEvaluationContext, params: Array<out NumberInterface>) =
            params[0] + params[1]

}