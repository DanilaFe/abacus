package org.nwapw.abacus.function

import org.nwapw.abacus.function.applicable.ReducerApplicable
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.tree.TreeNode

/**
 * An operator that operates on trees.
 *
 * This operator operates on parse trees, returning, however a number.
 * @param associativity the associativity of the operator.
 * @param type the type of the operator (infix, postfix, etc)
 * @param precedence the precedence of the operator.
 */
abstract class TreeValueOperator(associativity: OperatorAssociativity, type: OperatorType,
                                 precedence: Int) : ReducerApplicable<TreeNode, NumberInterface, NumberInterface>