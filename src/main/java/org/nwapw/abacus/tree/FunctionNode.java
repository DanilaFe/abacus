package org.nwapw.abacus.tree;

import java.util.ArrayList;
import java.util.List;

/**
 * A node that represents a function call.
 */
public class FunctionNode extends TreeNode {

    /**
     * The name of the function being called
     */
    private String function;
    /**
     * The list of arguments to the function.
     */
    private List<TreeNode> children;

    /**
     * Creates a function node with no function.
     */
    private FunctionNode() {
    }

    /**
     * Creates a new function node with the given function name.
     *
     * @param function the function name.
     */
    public FunctionNode(String function) {
        this.function = function;
        children = new ArrayList<>();
    }

    /**
     * Gets the function name for this node.
     *
     * @return the function name.
     */
    public String getFunction() {
        return function;
    }

    /**
     * Adds a child to the end of this node's child list.
     *
     * @param node the child to add.
     */
    public void appendChild(TreeNode node) {
        children.add(node);
    }

    /**
     * Adds a new child to the beginning of this node's child list.
     *
     * @param node the node to add.
     */
    public void prependChild(TreeNode node) {
        children.add(0, node);
    }

    @Override
    public <T> T reduce(Reducer<T> reducer) {
        if (Thread.currentThread().isInterrupted())
            return null;
        Object[] reducedChildren = new Object[children.size()];
        for (int i = 0; i < reducedChildren.length; i++) {
            reducedChildren[i] = children.get(i).reduce(reducer);
            if (Thread.currentThread().isInterrupted() || reducedChildren[i] == null) return null;
        }
        T a = reducer.reduceNode(this, reducedChildren);
        if (Thread.currentThread().isInterrupted())
            return null;
        return a;
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(function);
        buffer.append("(");
        for (int i = 0; i < children.size(); i++) {
            buffer.append(children.get(i));
            buffer.append(i == children.size() - 1 ? "" : ", ");
        }
        buffer.append(")");
        return buffer.toString();
    }
}
