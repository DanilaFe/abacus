package org.nwapw.abacus.function

import org.nwapw.abacus.function.applicable.ReducerApplicable
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.tree.TreeNode

/**
 * A function that operates on trees.
 *
 * A function that operates on parse tree nodes instead of on already simplified numbers.
 * Despite this, it returns a number, not a tree.
 */
abstract class TreeValueFunction : ReducerApplicable<TreeNode, NumberInterface, NumberInterface>
