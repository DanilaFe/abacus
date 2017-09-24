@file:JvmName("NumberUtils")
package org.nwapw.abacus.number.promotion

import org.nwapw.abacus.number.NumberInterface

typealias PromotionFunction = java.util.function.Function<NumberInterface, NumberInterface>
typealias PromotionPath = List<PromotionFunction>
typealias NumberClass = Class<NumberInterface>

/**
 * Promote a number through this path. The functions in this path
 * are applied in order to the number, and the final result is returned.
 *
 * @param from the number to start from.
 */
fun PromotionPath.promote(from: NumberInterface): NumberInterface {
    return fold(from, { current, function -> function.apply(current) })
}