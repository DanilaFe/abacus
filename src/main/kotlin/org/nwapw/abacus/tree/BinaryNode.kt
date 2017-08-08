package org.nwapw.abacus.tree

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