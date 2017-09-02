package org.nwapw.abacus.tree;

import org.nwapw.abacus.Abacus;
import org.nwapw.abacus.function.NumberFunction;
import org.nwapw.abacus.function.NumberOperator;
import org.nwapw.abacus.function.TreeValueFunction;
import org.nwapw.abacus.function.TreeValueOperator;
import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.number.PromotionManager;
import org.nwapw.abacus.number.PromotionResult;

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
        PromotionManager manager = abacus.getPromotionManager();
        if (node instanceof NumberNode) {
            return abacus.getNumberImplementation().instanceForString(((NumberNode) node).getNumber());
        } else if (node instanceof VariableNode) {
            return abacus.getVariableDatabase().getVariableValue(((VariableNode) node).getVariable());
        } else if (node instanceof NumberBinaryNode) {
            NumberInterface left = (NumberInterface) children[0];
            NumberInterface right = (NumberInterface) children[1];
            NumberOperator operator = abacus.getPluginManager().operatorFor(((BinaryNode) node).getOperation());
            PromotionResult result = manager.promote(left, right);
            if (result == null) return null;
            return operator.apply(result.getPromotedTo(), result.getItems());
        } else if (node instanceof NumberUnaryNode) {
            NumberInterface child = (NumberInterface) children[0];
            NumberOperator operator = abacus.getPluginManager().operatorFor(((UnaryNode) node).getOperation());
            return operator.apply(abacus.getPluginManager().interfaceImplementationFor(child.getClass()), child);
        } else if (node instanceof FunctionNode) {
            NumberInterface[] convertedChildren = new NumberInterface[children.length];
            for (int i = 0; i < convertedChildren.length; i++) {
                convertedChildren[i] = (NumberInterface) children[i];
            }
            NumberFunction function = abacus.getPluginManager().functionFor(((FunctionNode) node).getCallTo());
            if (function == null) return null;
            PromotionResult result = manager.promote(convertedChildren);
            if (result == null) return null;
            return function.apply(result.getPromotedTo(), result.getItems());
        } else if (node instanceof TreeValueFunctionNode) {
            CallNode callNode = (CallNode) node;
            TreeNode[] realChildren = new TreeNode[callNode.getChildren().size()];
            for (int i = 0; i < realChildren.length; i++) {
                realChildren[i] = callNode.getChildren().get(i);
            }
            TreeValueFunction function =
                    abacus.getPluginManager().treeValueFunctionFor(callNode.getCallTo());
            if (function == null) return null;
            return function.applyWithReducer(abacus.getNumberImplementation(), this, realChildren);
        } else if (node instanceof TreeValueBinaryNode) {
            BinaryNode binaryNode = (BinaryNode) node;
            TreeValueOperator operator = abacus.getPluginManager()
                    .treeValueOperatorFor(binaryNode.getOperation());
            if (operator == null) return null;
            return operator.applyWithReducer(abacus.getNumberImplementation(), this, binaryNode.getLeft(), binaryNode.getRight());
        } else if (node instanceof TreeValueUnaryNode) {
            UnaryNode unaryNode = (UnaryNode) node;
            TreeValueOperator operator = abacus.getPluginManager()
                    .treeValueOperatorFor(unaryNode.getOperation());
            if (operator == null) return null;
            return operator.applyWithReducer(abacus.getNumberImplementation(), this, unaryNode.getApplyTo());
        }
        return null;
    }

}
