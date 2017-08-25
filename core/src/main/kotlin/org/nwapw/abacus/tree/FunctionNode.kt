package org.nwapw.abacus.tree

/**
 * A tree node that holds a function call.
 *
 * The function call node can hold any number of children, and passes the to the appropriate reducer,
 * but that is its sole purpose.
 *
 * @param function the function string.
 */
class FunctionNode(val function: String) : TreeNode() {

    /**
     * List of function parameters added to this node.
     */
    val children: MutableList<TreeNode> = mutableListOf()

    override fun <T : Any> reduce(reducer: Reducer<T>): T? {
        val children = Array<Any>(children.size, { children[it].reduce(reducer) ?: return null; })
        return reducer.reduceNode(this, *children)
    }

    override fun toString(): String {
        val buffer = StringBuffer()
        buffer.append(function)
        buffer.append('(')
        for (i in 0 until children.size) {
            buffer.append(children[i].toString())
            buffer.append(if (i == children.size - 1) ")" else ",")
        }
        return buffer.toString()
    }

    /**
     * Appends a child to this node's list of children.
     *
     * @node the node to append.
     */
    fun appendChild(node: TreeNode) {
        children.add(node)
    }

    /**
     * Prepends a child to this node's list of children.
     *
     * @node the node to prepend.
     */
    fun prependChild(node: TreeNode) {
        children.add(0, node)
    }

}