package org.nwapw.abacus.tree

/**
 * Represents a more generic function call.
 *
 * This class does not specify how it should be reduced, allowing other classes
 * to extend this functionality.
 *
 * @param callTo the name of the things being called.
 * @param children the children of this node.
 */
abstract class CallNode(val callTo: String, val children: List<TreeNode>) : TreeNode() {

    override fun toString(): String {
        val buffer = StringBuffer()
        buffer.append(callTo)
        buffer.append("(")
        for (i in 0 until children.size) {
            buffer.append(children[i].toString())
            buffer.append(if (i != children.size - 1) ", " else ")")
        }
        return buffer.toString()
    }

}