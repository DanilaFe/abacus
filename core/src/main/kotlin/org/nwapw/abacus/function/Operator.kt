package org.nwapw.abacus.function

/**
 * A single operator that can be used by Abacus.
 *
 * This is a data class that holds the information about a single operator, such as a plus or minus.
 *
 * @param associativity the associativity of this operator, used for order of operations;.
 * @param type the type of this operator, used for parsing (infix / prefix / postfix and binary / unary)
 * @param precedence the precedence of this operator, used for order of operations.
 * @param function the function this operator applies to its arguments.
 */
data class Operator(val associativity: OperatorAssociativity, val type: OperatorType,
                    val precedence: Int, val function: Function)