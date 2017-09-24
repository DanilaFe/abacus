package org.nwapw.abacus.number.promotion

import org.nwapw.abacus.number.NumberInterface

/**
 * Function that is used to promote a number from one type to another.
 *
 * A promotion function is used in the promotion system as a mean to
 * actually "travel" down the promotion path.
 */
interface PromotionFunction {

    /**
     * Promotes the given [number] into another type.
     * @param number the number to promote from.
     * @return the new number with the same value.
     */
    fun promote(number: NumberInterface): NumberInterface

}
