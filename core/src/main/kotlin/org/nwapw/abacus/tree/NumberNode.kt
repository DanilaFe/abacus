package org.nwapw.abacus.tree

import org.nwapw.abacus.number.NumberInterface

/**
 * A tree node that holds a single number value.
 *
 * This is a tree node that holds a single NumberInterface, which represents any number,
 * and is not defined during compile time.
 *
 * @number the number value of this node.
 */
data class NumberNode(val number: NumberInterface) : TreeNode() {

    override fun <T : Any> reduce(reducer: Reducer<T>): T? {
        return reducer.reduceNode(this)
    }

    override fun toString(): String {
        return number.toString()
    }

}