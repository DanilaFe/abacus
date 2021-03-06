package org.nwapw.abacus.plugin.standard.operator

import org.nwapw.abacus.context.PluginEvaluationContext
import org.nwapw.abacus.function.OperatorAssociativity
import org.nwapw.abacus.function.OperatorType
import org.nwapw.abacus.function.interfaces.NumberOperator
import org.nwapw.abacus.number.NumberInterface

/**
 * The subtraction operator.
 *
 * This is a standard operator that performs subtraction.
 */
class OperatorSubtract: NumberOperator(OperatorAssociativity.LEFT, OperatorType.BINARY_INFIX, 1) {

    override fun matchesParams(context: PluginEvaluationContext, params: Array<out NumberInterface>) =
            params.size == 2
    override fun applyInternal(context: PluginEvaluationContext, params: Array<out NumberInterface>) =
            params[0] - params[1]

}