package org.nwapw.abacus.function

import org.nwapw.abacus.number.NumberInterface

/**
 * A function that operates on numbers.
 *
 * This function takes some number of input NumberInterfaces and returns
 * another NumberInterface as a result.
 */
abstract class NumberFunction : Applicable<NumberInterface, NumberInterface>()