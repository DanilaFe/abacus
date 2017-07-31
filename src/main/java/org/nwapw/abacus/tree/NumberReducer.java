package org.nwapw.abacus.tree;

import org.nwapw.abacus.Abacus;
import org.nwapw.abacus.function.Function;
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
            return ((NumberNode) node).getNumber();
        } else if (node instanceof BinaryInfixNode) {
            NumberInterface left = (NumberInterface) children[0];
            NumberInterface right = (NumberInterface) children[1];
            Function function = abacus.getPluginManager().operatorFor(((BinaryInfixNode) node).getOperation()).getFunction();
            if (function == null) return null;
            return function.apply(left, right);
        } else if (node instanceof UnaryPrefixNode) {
            NumberInterface child = (NumberInterface) children[0];
            Function functionn = abacus.getPluginManager().operatorFor(((UnaryPrefixNode) node).getOperation()).getFunction();
            if (functionn == null) return null;
            return functionn.apply(child);
        } else if (node instanceof FunctionNode) {
            NumberInterface[] convertedChildren = new NumberInterface[children.length];
            for (int i = 0; i < convertedChildren.length; i++) {
                convertedChildren[i] = (NumberInterface) children[i];
            }
            Function function = abacus.getPluginManager().functionFor(((FunctionNode) node).getFunction());
            if (function == null) return null;
            return function.apply(convertedChildren);
        }
        return null;
    }

}
