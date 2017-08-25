package org.nwapw.abacus.tree;

import org.nwapw.abacus.Abacus;
import org.nwapw.abacus.function.NumberFunction;
import org.nwapw.abacus.function.Operator;
import org.nwapw.abacus.function.TreeValueFunction;
import org.nwapw.abacus.number.NumberInterface;

/**
 * A reducer implementation that turns a tree into a single number.
 * This is not always guaranteed to work.
 */
public class NumberReducer implements Reducer<NumberInterface> {

    /**
     * The plugin manager from which to draw the functions.
     */
    private Abacus abacus;

    /**
     * Creates a new number reducer.
     *
     * @param abacus the calculator instance.
     */
    public NumberReducer(Abacus abacus) {
        this.abacus = abacus;
    }

    @Override
    public NumberInterface reduceNode(TreeNode node, Object... children) {
        if (node instanceof NumberNode) {
            return abacus.numberFromString(((NumberNode) node).getNumber());
        } else if(node instanceof VariableNode) {
            return abacus.numberFromString("0");
        } else if (node instanceof BinaryNode) {
            NumberInterface left = (NumberInterface) children[0];
            NumberInterface right = (NumberInterface) children[1];
            Operator<NumberInterface, NumberInterface> operator = abacus.getPluginManager().operatorFor(((BinaryNode) node).getOperation());
            return operator.apply(left, right);
        } else if (node instanceof UnaryNode) {
            NumberInterface child = (NumberInterface) children[0];
            Operator<NumberInterface, NumberInterface> operator = abacus.getPluginManager().operatorFor(((UnaryNode) node).getOperation());
            return operator.apply(child);
        } else if (node instanceof FunctionNode) {
            NumberInterface[] convertedChildren = new NumberInterface[children.length];
            for (int i = 0; i < convertedChildren.length; i++) {
                convertedChildren[i] = (NumberInterface) children[i];
            }
            NumberFunction function = abacus.getPluginManager().functionFor(((FunctionNode) node).getCallTo());
            if (function == null) return null;
            return function.apply(convertedChildren);
        } else if (node instanceof TreeValueFunctionNode){
            CallNode callNode = (CallNode) node;
            TreeNode[] realChildren = new TreeNode[callNode.getChildren().size()];
            for(int i = 0; i < realChildren.length; i++){
                realChildren[i] = callNode.getChildren().get(i);
            }
            TreeValueFunction function =
                    abacus.getPluginManager().treeValueFunctionFor(callNode.getCallTo());
            if(function == null) return null;
            return function.applyWithReducer(this, realChildren);
        }
        return null;
    }

}
