package org.nwapw.abacus.tree

/**
 * A tree node that represents a unary tree value operator.
 *
 * The tree value operators operate on trees, and so this
 * node does not reduce its children. It is up to the implementation to handle
 * reduction.
 * @param operation the operation this node performs.
 * @param child the node the operation should be applied to.
 */
class TreeValueUnaryNode(operation: String, child: TreeNode)
    : UnaryNode(operation, child) {

    override fun <T : Any> reduce(reducer: Reducer<T>): T {
        return reducer.reduceNode(this)
    }

}