package org.nwapw.abacus.function

/**
 * A single operator that can be used by Abacus.
 *
 * This is a class that holds the information about a single operator, such as a plus or minus.
 *
 * @param associativity the associativity of this operator, used for order of operations;.
 * @param type the type of this operator, used for parsing (infix / prefix / postfix and binary / unary)
 * @param precedence the precedence of this operator, used for order of operations.
 */
abstract class Operator<T: Any, O: Any>(val associativity: OperatorAssociativity, val type: OperatorType,
                                               val precedence: Int) : Applicable<T, O>()