package org.nwapw.abacus.plugin.standard

import org.nwapw.abacus.context.MutableEvaluationContext
import org.nwapw.abacus.function.NumberOperator
import org.nwapw.abacus.function.OperatorAssociativity
import org.nwapw.abacus.function.OperatorType
import org.nwapw.abacus.number.NumberInterface

/**
 * The division operator.
 *
 * This is a standard operator that simply performs division.
 */
class OperatorDivide: NumberOperator(OperatorAssociativity.LEFT, OperatorType.BINARY_INFIX, 1) {

    override fun matchesParams(context: MutableEvaluationContext, params: Array<out NumberInterface>) =
            params.size == 2
    override fun applyInternal(context: MutableEvaluationContext, params: Array<out NumberInterface>) =
            params[0] / params[1]

}