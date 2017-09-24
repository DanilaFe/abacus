package org.nwapw.abacus.tree.nodes

/**
 * A tree node that holds a unary operation.
 *
 * This node holds a single operator applied to a single parameter, and does not care
 * whether the operation was found before or after the parameter in the text.
 *
 * @param operation the operation applied to the given node.
 * @param applyTo the node to which the operation will be applied.
 */
abstract class UnaryNode(val operation: String, val applyTo: TreeNode) : TreeNode() {

    override fun toString(): String {
        return "(" + applyTo.toString() + ")" + operation
    }

}