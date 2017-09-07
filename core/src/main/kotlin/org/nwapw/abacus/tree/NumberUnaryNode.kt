package org.nwapw.abacus.tree

/**
 * A unary operator node that reduces its children.
 *
 * NumberUnaryNode operates by simply reducing its child,
 * and using the result of that reduction to reduce itself.
 * @param operation the operation this node performs.
 * @param child the child this node should be applied to.
 */
class NumberUnaryNode(operation: String, child: TreeNode)
    : UnaryNode(operation, child) {

    override fun <T : Any> reduce(reducer: Reducer<T>): T? {
        val child = applyTo.reduce(reducer)
        return reducer.reduceNode(this, child)
    }

}