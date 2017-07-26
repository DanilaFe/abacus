package org.nwapw.abacus.tree;

import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.plugin.PluginManager;

public class NumberReducer implements Reducer<NumberInterface> {

    private PluginManager manager;

    public NumberReducer(PluginManager manager){
        this.manager = manager;
    }

    @Override
    public NumberInterface reduceNode(TreeNode node, Object... children) {
        if(node instanceof NumberNode) {
            return ((NumberNode) node).getNumber();
        } else if(node instanceof OpNode){
            NumberInterface left = (NumberInterface) children[0];
            NumberInterface right = (NumberInterface) children[1];
            return manager.functionFor(((OpNode) node).getOperation()).apply(left, right);
        }
        return null;
    }

}
