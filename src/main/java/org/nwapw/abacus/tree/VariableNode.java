package org.nwapw.abacus.tree;

import org.nwapw.abacus.number.NumberInterface;

public class VariableNode extends TreeNode{
    private String variable;
    public VariableNode() {
        variable = null;
    }
    public VariableNode(String name){
        this.variable = name;
    }
    public String getVariable() {
        return variable;
    }
    @Override
    public <T> T reduce(Reducer<T> reducer) {
        return reducer.reduceNode(this);
    }
    @Override
    public String toString() {
        return variable;
    }
}
