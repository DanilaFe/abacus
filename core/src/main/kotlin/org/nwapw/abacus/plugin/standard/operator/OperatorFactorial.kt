package org.nwapw.abacus.plugin.standard.operator

import org.nwapw.abacus.context.PluginEvaluationContext
import org.nwapw.abacus.function.OperatorAssociativity
import org.nwapw.abacus.function.OperatorType
import org.nwapw.abacus.function.interfaces.NumberOperator
import org.nwapw.abacus.number.NumberInterface

/**
 * The factorial operator.
 *
 * This is a standard operator that simply evaluates the factorial of a number.
 */
class OperatorFactorial: NumberOperator(OperatorAssociativity.LEFT, OperatorType.UNARY_POSTFIX, 0) {

    override fun matchesParams(context: PluginEvaluationContext, params: Array<out NumberInterface>) =
        params.size == 1
                && params[0].isInteger()
                && params[0].signum() >= 0

    override fun applyInternal(context: PluginEvaluationContext, params: Array<out NumberInterface>): NumberInterface {
        val implementation = context.inheritedNumberImplementation
        val one = implementation.instanceForString("1")
        if (params[0].signum() == 0) {
            return one
        }
        var factorial = params[0]
        var multiplier = params[0] - one
        //It is necessary to later prevent calls of factorial on anything but non-negative integers.
        while (multiplier.signum() == 1) {
            factorial *= multiplier
            multiplier -= one
        }
        return factorial
    }

}