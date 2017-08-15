package org.nwapw.abacus.tree

/**
 * A tree node.
 */
abstract class TreeNode {

    abstract fun <T : Any> reduce(reducer: Reducer<T>): T?

}