package org.nwapw.abacus.function

import org.nwapw.abacus.function.applicable.Applicable
import org.nwapw.abacus.number.NumberInterface

/**
 * An operator that operates on NumberImplementations.
 *
 * This is simply an alias for Operator<NumberInterface, NumberInterface>.
 * @param associativity the associativity of the operator.
 * @param type the type of the operator (binary, unary, etc)
 * @param precedence the precedence of the operator.
 */
abstract class NumberOperator(associativity: OperatorAssociativity, type: OperatorType,
                              precedence: Int) :
        Operator(associativity, type, precedence),
        Applicable<NumberInterface, NumberInterface>