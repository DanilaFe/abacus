package org.nwapw.abacus.plugin.standard.operator

import org.nwapw.abacus.context.PluginEvaluationContext
import org.nwapw.abacus.function.OperatorAssociativity
import org.nwapw.abacus.function.OperatorType
import org.nwapw.abacus.function.interfaces.NumberOperator
import org.nwapw.abacus.number.NumberInterface

/**
 * The "N pick R" operator.
 *
 * his is a standard operator that returns the number of possible combinations
 * of a certain size can be taken out of a pool of a bigger size.
 */
class OperatorNpr: NumberOperator(OperatorAssociativity.RIGHT, OperatorType.BINARY_INFIX, 1) {

    override fun matchesParams(context: PluginEvaluationContext, params: Array<out NumberInterface>) =
            params.size == 2 && params[0].isInteger()
                    && params[1].isInteger()

    override fun applyInternal(context: PluginEvaluationContext, params: Array<out NumberInterface>): NumberInterface {
        val implementation = context.inheritedNumberImplementation
        if (params[0] < params[1] ||
                params[0].signum() < 0 ||
                params[0].signum() == 0 && params[1].signum() != 0)
            return implementation.instanceForString("0")
        var total = implementation.instanceForString("1")
        var multiplyBy = params[0]
        var remainingMultiplications = params[1]
        val halfway = params[0] / implementation.instanceForString("2")
        if (remainingMultiplications > halfway) {
            remainingMultiplications = params[0] - remainingMultiplications
        }
        while (remainingMultiplications.signum() > 0) {
            total *= multiplyBy
            remainingMultiplications -= implementation.instanceForString("1")
            multiplyBy -= implementation.instanceForString("1")
        }
        return total
    }

}