package org.nwapw.abacus.plugin.standard

import org.nwapw.abacus.context.MutableEvaluationContext
import org.nwapw.abacus.function.NumberOperator
import org.nwapw.abacus.function.OperatorAssociativity
import org.nwapw.abacus.function.OperatorType
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.plugin.StandardPlugin.*

/**
 * The "N choose R" operator.
 *
 * This is a standard operator that returns the number of possible combinations, regardless of order,
 * of a certain size can be taken out of a pool of a bigger size.
 */
class OperatorNcr: NumberOperator(OperatorAssociativity.RIGHT, OperatorType.BINARY_INFIX, 0) {

    override fun matchesParams(context: MutableEvaluationContext, params: Array<out NumberInterface>) =
            params.size == 2 && params[0].fractionalPart().signum() == 0
                    && params[1].fractionalPart().signum() == 0

    override fun applyInternal(context: MutableEvaluationContext, params: Array<out NumberInterface>) =
            OP_NPR.apply(context, *params) / OP_FACTORIAL.apply(context, params[1])

}