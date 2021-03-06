package org.nwapw.abacus.plugin.standard.operator

import org.nwapw.abacus.context.PluginEvaluationContext
import org.nwapw.abacus.function.OperatorAssociativity
import org.nwapw.abacus.function.OperatorType
import org.nwapw.abacus.function.interfaces.NumberOperator
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.plugin.standard.StandardPlugin.OP_FACTORIAL
import org.nwapw.abacus.plugin.standard.StandardPlugin.OP_NPR

/**
 * The "N choose R" operator.
 *
 * This is a standard operator that returns the number of possible combinations, regardless of order,
 * of a certain size can be taken out of a pool of a bigger size.
 */
class OperatorNcr: NumberOperator(OperatorAssociativity.RIGHT, OperatorType.BINARY_INFIX, 1) {

    override fun matchesParams(context: PluginEvaluationContext, params: Array<out NumberInterface>) =
            params.size == 2 && params[0].isInteger()
                    && params[1].isInteger()

    override fun applyInternal(context: PluginEvaluationContext, params: Array<out NumberInterface>) =
            OP_NPR.apply(context, *params) / OP_FACTORIAL.apply(context, params[1])

}