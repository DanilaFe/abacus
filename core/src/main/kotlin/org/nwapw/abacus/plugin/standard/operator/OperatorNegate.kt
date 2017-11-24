package org.nwapw.abacus.plugin.standard.operator

import org.nwapw.abacus.context.PluginEvaluationContext
import org.nwapw.abacus.function.OperatorAssociativity
import org.nwapw.abacus.function.OperatorType
import org.nwapw.abacus.function.interfaces.NumberOperator
import org.nwapw.abacus.number.NumberInterface

/**
 * The negation operator.
 *
 * This is a standard operator that negates a number.
 */
class OperatorNegate: NumberOperator(OperatorAssociativity.LEFT, OperatorType.UNARY_PREFIX, 0) {

    override fun matchesParams(context: PluginEvaluationContext, params: Array<out NumberInterface>) =
            params.size == 1

    override fun applyInternal(context: PluginEvaluationContext, params: Array<out NumberInterface>) =
            -params[0]

}