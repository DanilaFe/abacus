package org.nwapw.abacus.tree

/**
 * A tree node that holds a binary operation.
 *
 * This node represents any binary operation, such as binary infix or binary postfix. The only
 * currently implemented into Abacus is binary infix, but that has more to do with the parser than
 * this class, which doesn't care about the order that its operation and nodes were found in text.
 *
 * @param operation the operation this node performs on its children.
 * @param left the left node.
 * @param right the right node.
 */
data class BinaryNode(val operation: String, val left: TreeNode? = null, val right: TreeNode?) : TreeNode() {

    override fun <T : Any> reduce(reducer: Reducer<T>): T? {
        val leftReduce = left?.reduce(reducer) ?: return null
        val rightReduce = right?.reduce(reducer) ?: return null
        return reducer.reduceNode(this, leftReduce, rightReduce)
    }

    override fun toString(): String {
        return "(" + (left?.toString() ?: "null") + operation + (right?.toString() ?: "null") + ")"
    }

}