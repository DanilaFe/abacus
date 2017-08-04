package org.nwapw.abacus.tree;

/**
 * A tree node that represents an operation being applied to two operands.
 */
public class BinaryNode extends TreeNode {

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

    private BinaryNode() {
    }

    /**
     * Creates a new operation node with the given operation
     * and no child nodes.
     *
     * @param operation the operation.
     */
    public BinaryNode(String operation) {
        this(operation, null, null);
    }

    /**
     * Creates a new operation node with the given operation
     * and child nodes.
     *
     * @param operation the operation.
     * @param left      the left node of the expression.
     * @param right     the right node of the expression.
     */
    public BinaryNode(String operation, TreeNode left, TreeNode right) {
        this.operation = operation;
        this.left = left;
        this.right = right;
    }

    /**
     * Gets the operation in this node.
     *
     * @return the operation in this node.
     */
    public String getOperation() {
        return operation;
    }

    /**
     * Gets the left sub-expression of this node.
     *
     * @return the left node.
     */
    public TreeNode getLeft() {
        return left;
    }

    /**
     * Sets the left sub-expression of this node.
     *
     * @param left the sub-expression to apply.
     */
    public void setLeft(TreeNode left) {
        this.left = left;
    }

    /**
     * Gets the right sub-expression of this node.
     *
     * @return the right node.
     */
    public TreeNode getRight() {
        return right;
    }

    /**
     * Sets the right sub-expression of this node.
     *
     * @param right the sub-expression to apply.
     */
    public void setRight(TreeNode right) {
        this.right = right;
    }

    @Override
    public <T> T reduce(Reducer<T> reducer) {
        if (Thread.currentThread().isInterrupted())
            return null;
        T leftReduce = left.reduce(reducer);
        T rightReduce = right.reduce(reducer);
        if (leftReduce == null || rightReduce == null) return null;
        T a = reducer.reduceNode(this, leftReduce, rightReduce);
        if (Thread.currentThread().isInterrupted())
            return null;
        return a;
    }

    @Override
    public String toString() {
        String leftString = left != null ? left.toString() : "null";
        String rightString = right != null ? right.toString() : "null";

        return "(" + leftString + operation + rightString + ")";
    }
}
