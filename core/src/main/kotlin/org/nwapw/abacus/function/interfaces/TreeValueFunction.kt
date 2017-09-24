package org.nwapw.abacus.function.interfaces

import org.nwapw.abacus.function.Applicable
import org.nwapw.abacus.number.NumberInterface
import org.nwapw.abacus.tree.nodes.TreeNode

/**
 * A function that operates on trees.
 *
 * A function that operates on parse tree nodes instead of on already simplified numbers.
 * Despite this, it returns a number, not a tree.
 */
abstract class TreeValueFunction : Applicable<TreeNode, NumberInterface>
