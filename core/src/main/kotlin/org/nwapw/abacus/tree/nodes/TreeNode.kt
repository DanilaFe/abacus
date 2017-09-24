package org.nwapw.abacus.tree.nodes

import org.nwapw.abacus.tree.Reducer

/**
 * A tree node.
 */
abstract class TreeNode {

    abstract fun <T : Any> reduce(reducer: Reducer<T>): T

}