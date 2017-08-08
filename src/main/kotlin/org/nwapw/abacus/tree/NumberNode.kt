package org.nwapw.abacus.tree

import org.nwapw.abacus.number.NumberInterface

data class NumberNode(val number: NumberInterface) : TreeNode() {

    override fun <T : Any> reduce(reducer: Reducer<T>): T? {
        return reducer.reduceNode(this)
    }

    override fun toString(): String {
        return number.toString()
    }

}