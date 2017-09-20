package org.nwapw.abacus.number

import org.nwapw.abacus.Abacus

/**
 * A utility class for creating [NumberRange] instances.
 *
 * Unlike a regular [ClosedRange], a NumberRange must have a third parameter,
 * which is the [Abacus] instance that is used for promotion. However, the ".." operator
 * is infix, and can only take two parameters. The solution is, instead of returning instances
 * of NumberRange directly, to return a builder, which then provides a [with] infix function
 * to attach it to an instance of Abacus.
 * @property start the beginning of the range.
 * @property endInclusive the end of the range.
 */
class NumberRangeBuilder(private val start: NumberInterface, private val endInclusive: NumberInterface) {

    /**
     * Generate a [NumberRange] with the given instance of [abacus].
     * @return a new range with the given instance of Abacus.
     */
    infix fun with(abacus: Abacus) = NumberRange(abacus, start, endInclusive)

}
