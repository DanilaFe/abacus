package org.nwapw.abacus.tree

data class FunctionNode(val function: String) : TreeNode() {

    val children: MutableList<TreeNode> = mutableListOf()

    override fun <T : Any> reduce(reducer: Reducer<T>): T? {
        val children = Array<Any?>(children.size, { children[it].reduce(reducer) ?: return null; })
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
        return super.toString()
    }

    fun appendChild(node: TreeNode){
        children.add(node)
    }

    fun prependChild(node: TreeNode){
        children.add(0, node)
    }

}