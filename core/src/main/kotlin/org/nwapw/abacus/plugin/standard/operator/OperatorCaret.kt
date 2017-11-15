package org.nwapw.abacus.plugin.standard.operator

import org.nwapw.abacus.context.MutableEvaluationContext
import org.nwapw.abacus.function.OperatorAssociativity
import org.nwapw.abacus.function.OperatorType
import org.nwapw.abacus.function.interfaces.NumberOperator
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.plugin.standard.StandardPlugin.*

/**
 * The power operator.
 *
 * This is a standard operator that brings one number to the power of the other.
 */
class OperatorCaret: NumberOperator(OperatorAssociativity.RIGHT, OperatorType.BINARY_INFIX, 2) {

    override fun matchesParams(context: MutableEvaluationContext, params: Array<out NumberInterface>) =
            params.size == 2
                    && !(params[0].signum() == 0 && params[1].signum() == 0)
                    && !(params[0].signum() == -1 && params[1].fractionalPart().signum() != 0)

    override fun applyInternal(context: MutableEvaluationContext, params: Array<out NumberInterface>): NumberInterface {
        val implementation = context.inheritedNumberImplementation
        if (params[0].signum() == 0)
            return implementation.instanceForString("0")
        else if (params[1].signum() == 0)
            return implementation.instanceForString("1")
        //Detect integer bases:
        if (params[0].fractionalPart().signum() == 0
                && FUNCTION_ABS.apply(context, params[1]) < implementation.instanceForString(Integer.toString(Integer.MAX_VALUE))
                && FUNCTION_ABS.apply(context, params[1]) >= implementation.instanceForString("1")) {
            val newParams = arrayOf(params[0], params[1].fractionalPart())
            return params[0].intPow(params[1].floor().intValue()) * applyInternal(context, newParams)
        }
        return FUNCTION_EXP.apply(context, FUNCTION_LN.apply(context, FUNCTION_ABS.apply(context, params[0])) * params[1])
    }

}