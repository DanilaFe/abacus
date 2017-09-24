package org.nwapw.abacus.number.range

import org.nwapw.abacus.Abacus
import org.nwapw.abacus.number.NumberInterface

/**
 * A closed range designed specifically for [NumberInterface]
 *
 * Besides providing the usual functionality of a [ClosedRange], this range
 * also handles promotion - that is, it's safe to use it with numbers of different
 * implementations, even as starting and ending points.
 *
 * @property abacus the abacus instance used for promotion.
 * @property start the starting point of the range.
 * @property endInclusive the ending point of the range.
 */
class NumberRange(val abacus: Abacus,
                  override val start: NumberInterface,
                  override val endInclusive: NumberInterface): ClosedRange<NumberInterface> {

    override operator fun contains(value: NumberInterface): Boolean {
        val promotionResult = abacus.promotionManager.promote(start, endInclusive, value)
        val newStart = promotionResult.items[0]
        val newEnd = promotionResult.items[1]
        val newValue = promotionResult.items[2]
        return newValue >= newStart && newValue <= newEnd
    }

}