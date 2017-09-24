package org.nwapw.abacus.tree.nodes

import org.nwapw.abacus.tree.Reducer

/**
 * A tree node that holds a placeholder variable.
 *
 * This node holds a variable string, and acts similarly to a number,
 * with the key difference of not actually holding a value at runtime.
 *
 * @param variable the actual variable name that this node represents.
 */
class VariableNode(val variable: String) : TreeNode() {

    override fun <T : Any> reduce(reducer: Reducer<T>): T {
        return reducer.reduceNode(this)
    }

    override fun toString(): String {
        return variable
    }

}