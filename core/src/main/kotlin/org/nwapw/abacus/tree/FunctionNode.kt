package org.nwapw.abacus.tree

/**
 * A tree node that holds a function call.
 *
 * The function call node can hold any number of children, and passes the to the appropriate reducer,
 * but that is its sole purpose.
 *
 * @param function the function string.
 */
class FunctionNode(function: String, children: List<TreeNode>) : CallNode(function, children) {

    override fun <T : Any> reduce(reducer: Reducer<T>): T {
        val children = Array<Any>(children.size, { children[it].reduce(reducer) })
        return reducer.reduceNode(this, *children)
    }

}