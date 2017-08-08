package org.nwapw.abacus.tree

abstract class TreeNode {

    abstract fun <T: Any> reduce(reducer: Reducer<T>) : T?

}