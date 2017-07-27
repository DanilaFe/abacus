package org.nwapw.abacus.tree;

import java.util.ArrayList;

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
    private ArrayList<TreeNode> children;

    /**
     * Creates a function node with no function.
     */
    private FunctionNode() { }

    /**
     * Creates a new function node with the given function name.
     * @param function the function name.
     */
    public FunctionNode(String function){
        this.function = function;
        children = new ArrayList<>();
    }

    /**
     * Gets the function name for this node.
     * @return the function name.
     */
    public String getFunction() {
        return function;
    }

    /**
     * Adds a child to this node.
     * @param node the child to add.
     */
    public void addChild(TreeNode node){
        children.add(node);
    }

    @Override
    public <T> T reduce(Reducer<T> reducer) {
        Object[] reducedChildren = new Object[children.size()];
        for(int i = 0; i < reducedChildren.length; i++){
            reducedChildren[i] = children.get(i).reduce(reducer);
            if(reducedChildren[i] == null) return null;
        }
        return reducer.reduceNode(this, reducedChildren);
    }

    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append(function);
        buffer.append("(");
        for(int i = 0; i < children.size(); i++){
            buffer.append(children.get(i));
            buffer.append(i == children.size() - 1 ? "" : ", ");
        }
        buffer.append(")");
        return buffer.toString();
    }
}
