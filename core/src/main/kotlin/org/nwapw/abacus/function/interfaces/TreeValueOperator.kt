package org.nwapw.abacus.function.interfaces

import org.nwapw.abacus.function.Applicable
import org.nwapw.abacus.function.Operator
import org.nwapw.abacus.function.OperatorAssociativity
import org.nwapw.abacus.function.OperatorType
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.tree.nodes.TreeNode

/**
 * An operator that operates on trees.
 *
 * This operator operates on parse trees, returning, however a number.
 * @param associativity the associativity of the operator.
 * @param type the type of the operator (infix, postfix, etc)
 * @param precedence the precedence of the operator.
 */
abstract class TreeValueOperator(associativity: OperatorAssociativity, type: OperatorType,
                                 precedence: Int) :
        Operator(associativity, type, precedence),
        Applicable<TreeNode, NumberInterface>