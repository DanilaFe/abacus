package org.nwapw.abacus.tree;

import org.nwapw.abacus.Abacus;

public class UnaryPrefixNode extends TreeNode {

    /**
     * The operation this node will apply.
     */
    private String operation;
    /**
     * The tree node to apply the operation to.
     */
    private TreeNode applyTo;
    private Abacus trace;

    /**
     * Creates a new node with the given operation and no child.
     *
     * @param operation the operation for this node.
     */
    public UnaryPrefixNode(String operation,Abacus trace) {
        this(operation, null,trace);
    }

    /**
     * Creates a new node with the given operation and child.
     *
     * @param operation the operation for this node.
     * @param applyTo   the node to apply the function to.
     */
    public UnaryPrefixNode(String operation, TreeNode applyTo,Abacus trace) {
        this.operation = operation;
        this.applyTo = applyTo;
        this.trace = trace;
    }

    @Override
    public <T> T reduce(Reducer<T> reducer) {
        if(!trace.getStop()) {
            Object reducedChild = applyTo.reduce(reducer);
            if (reducedChild == null) return null;
            return reducer.reduceNode(this, reducedChild);
        }
        return null;
    }

    /**
     * Gets the operation of this node.
     *
     * @return the operation this node performs.
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Gets the node to which this node's operation applies.
     *
     * @return the tree node to which the operation will be applied.
     */
    public TreeNode getApplyTo() {
        return applyTo;
    }

    @Override
    public String toString() {
        return "(" + (applyTo == null ? "null" : applyTo.toString()) + ")" + operation;
    }
}
