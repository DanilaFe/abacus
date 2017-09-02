package org.nwapw.abacus.number

import org.nwapw.abacus.plugin.NumberImplementation

/**
 * The result of promoting an array of NumberInterfaces.
 *
 * @param promotedTo the implementation to which the numbers were promoted.
 * @param items the items the items resulting from the promotion.
 */
data class PromotionResult(val promotedTo: NumberImplementation, val items: Array<NumberInterface>)