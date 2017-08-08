package org.nwapw.abacus.tree

data class UnaryNode(val operation: String, val applyTo: TreeNode? = null) : TreeNode() {

    override fun <T : Any> reduce(reducer: Reducer<T>): T? {
        val reducedChild = applyTo?.reduce(reducer) ?: return null
        return reducer.reduceNode(this, reducedChild)
    }

}