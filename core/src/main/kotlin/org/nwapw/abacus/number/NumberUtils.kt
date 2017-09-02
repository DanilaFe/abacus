@file:JvmName("NumberUtils")
package org.nwapw.abacus.number

typealias PromotionFunction = java.util.function.Function<NumberInterface, NumberInterface>
typealias PromotionPath = List<PromotionFunction>
typealias NumberClass = Class<NumberInterface>

fun PromotionPath.promote(from: NumberInterface): NumberInterface {
    return fold(from, { current, function -> function.apply(current) })
}