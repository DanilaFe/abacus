package org.nwapw.abacus.tree

/**
 * A tree node that represents a binary tree value operator.
 *
 *
 * The tree value operators operate on trees, and so this
 * node does not reduce its children. It is up to the implementation to handle
 * reduction.
 * @param operation the operation this node performs.
 * @param left the left child of this node.
 * @param right the right child of this node.
 */
class TreeValueBinaryNode(operation: String, left: TreeNode, right: TreeNode)
    : BinaryNode(operation, left, right) {

    override fun <T : Any> reduce(reducer: Reducer<T>): T {
        return reducer.reduceNode(this)
    }

}