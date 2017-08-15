package org.nwapw.abacus.tree

/**
 * Reducer interface that takes a tree and returns a single value.
 *
 * The reducer walks the tree, visiting the children first, converting them into
 * a value, and then attempts to reduce the parent. Eventually, the single final value is returned.
 */
interface Reducer<out T> {

    /**
     * Reduces the given tree node, given its already reduced children.
     *
     * @param treeNode the tree node to reduce.
     * @param children the list of children, of type T.
     */
    fun reduceNode(treeNode: TreeNode, vararg children: Any): T?

}