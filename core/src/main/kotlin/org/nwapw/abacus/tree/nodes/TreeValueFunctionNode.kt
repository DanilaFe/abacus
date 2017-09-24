package org.nwapw.abacus.tree.nodes

import org.nwapw.abacus.tree.Reducer

/**
 * A tree node that represents a tree value function call.
 *
 * This is in many ways similar to a simple FunctionNode, and the distinction
 * is mostly to help the reducer. Besides that, this class also does not
 * even attempt to reduce its children.
 */
class TreeValueFunctionNode(name: String, children: List<TreeNode>) : CallNode(name, children) {

    override fun <T : Any> reduce(reducer: Reducer<T>): T {
        return reducer.reduceNode(this)
    }

}
