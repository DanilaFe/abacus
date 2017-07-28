package org.nwapw.abacus.tree;

public class UnaryPrefixNode extends TreeNode {

    /**
     * The operation this node will apply.
     */
    private String operation;
    /**
     * The tree node to apply the operation to.
     */
    private TreeNode applyTo;

    /**
     * Creates a new node with the given operation and no child.
     * @param operation the operation for this node.
     */
    public UnaryPrefixNode(String operation){
        this(operation, null);
    }

    /**
     * Creates a new node with the given operation and child.
     * @param operation the operation for this node.
     * @param applyTo the node to apply the function to.
     */
    public UnaryPrefixNode(String operation, TreeNode applyTo){
        this.operation = operation;
        this.applyTo = applyTo;
    }

    @Override
    public <T> T reduce(Reducer<T> reducer) {
        Object reducedChild = applyTo.reduce(reducer);
        if(reducedChild == null) return null;
        return reducer.reduceNode(this, reducedChild);
    }

    /**
     * Gets the operation of this node.
     * @return the operation this node performs.
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Gets the node to which this node's operation applies.
     * @return the tree node to which the operation will be applied.
     */
    public TreeNode getApplyTo() {
        return applyTo;
    }
}
