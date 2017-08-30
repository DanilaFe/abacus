package org.nwapw.abacus.tree

/**
 * A binary operator node that reduces its children.
 *
 * NumberBinaryNode operates by simply reducing its children and
 * then using the result of that reduction to reduce itself.
 *
 * @param operation the operation this node performs.
 * @param left the left child of this node.
 * @param right the right child of this node.
 */
class NumberBinaryNode(operation: String, left: TreeNode?, right: TreeNode?)
    : BinaryNode(operation, left, right) {

    override fun <T : Any> reduce(reducer: Reducer<T>): T? {
        val left = left?.reduce(reducer) ?: return null
        val right = right?.reduce(reducer) ?: return null
        return reducer.reduceNode(this, left, right)
    }

}