package org.nwapw.abacus.tree;

import org.nwapw.abacus.function.Function;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.plugin.PluginManager;

/**
 * A reducer implementation that turns a tree into a single number.
 * This is not always guaranteed to work.
 */
public class NumberReducer implements Reducer<NumberInterface> {

    /**
     * The plugin manager from which to draw the functions.
     */
    private PluginManager manager;

    /**
     * Creates a new number reducer with the given plugin manager.
     * @param manager the plugin manager.
     */
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
            Function function = manager.operatorFor(((OpNode) node).getOperation()).getFunction();
            if(function == null) return null;
            return function.apply(left, right);
        } else if(node instanceof FunctionNode){
            NumberInterface[] convertedChildren = new NumberInterface[children.length];
            for(int i = 0; i < convertedChildren.length; i++){
                convertedChildren[i] = (NumberInterface) children[i];
            }
            Function function = manager.functionFor(((FunctionNode) node).getFunction());
            if(function == null) return null;
            return function.apply(convertedChildren);
        }
        return null;
    }

}
