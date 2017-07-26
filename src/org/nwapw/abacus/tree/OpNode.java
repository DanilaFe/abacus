package org.nwapw.abacus.tree;

/**
 * A tree node that represents an operation being applied to two operands.
 */
public class OpNode extends TreeNode {

    /**
     * The operation being applied.
     */
    private String operation;
    /**
     * The left node of the operation.
     */
    private TreeNode left;
    /**
     * The right node of the operation.
     */
    private TreeNode right;

    private OpNode() {}

    /**
     * Creates a new operation node with the given operation
     * and no child nodes.
     * @param operation the operation.
     */
    public OpNode(String operation){
        this(operation, null, null);
    }

    /**
     * Creates a new operation node with the given operation
     * and child nodes.
     * @param operation the operation.
     * @param left the left node of the expression.
     * @param right the right node of the expression.
     */
    public OpNode(String operation, TreeNode left, TreeNode right){
        this.operation = operation;
        this.left = left;
        this.right = right;
    }

    /**
     * Gets the operation in this node.
     * @return the operation in this node.
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Gets the left sub-expression of this node.
     * @return the left node.
     */
    public TreeNode getLeft() {
        return left;
    }

    /**
     * Sets the left sub-expression of this node.
     * @param left the sub-expression to apply.
     */
    public void setLeft(TreeNode left) {
        this.left = left;
    }

    /**
     * Gets the right sub-expression of this node.
     * @return the right node.
     */
    public TreeNode getRight() {
        return right;
    }

    /**
     * Sets the right sub-expression of this node.
     * @param right the sub-expression to apply.
     */
    public void setRight(TreeNode right) {
        this.right = right;
    }

    @Override
    public <T> T reduce(Reducer<T> reducer) {
        T leftReduce = left.reduce(reducer);
        T rightReduce = right.reduce(reducer);
        return reducer.reduceNode(this, leftReduce, rightReduce);
    }
}
