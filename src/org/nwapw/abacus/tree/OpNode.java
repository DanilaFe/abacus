package org.nwapw.abacus.tree;

public class OpNode extends TreeNode {

    private String operation;
    private TreeNode left;
    private TreeNode right;

    public OpNode(String operation){
        this(operation, null, null);
    }

    public OpNode(String operation, TreeNode left, TreeNode right){
        this.operation = operation;
        this.left = left;
        this.right = right;
    }

    public String getOperation() {
        return operation;
    }

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }
}
