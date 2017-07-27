package org.nwapw.abacus.tree;

import java.util.ArrayList;

public class FunctionNode extends TreeNode {

    private String function;
    private ArrayList<TreeNode> children;

    private FunctionNode() { }

    public FunctionNode(String function){
        this.function = function;
        children = new ArrayList<>();
    }

    public String getFunction() {
        return function;
    }

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
