package org.nwapw.abacus.function;

import org.nwapw.abacus.number.NumberInterface;
import org.nwapw.abacus.tree.Reducer;
import org.nwapw.abacus.tree.TreeNode;

/**
 * A function that operates on parse tree nodes instead of on already simplified numbers.
 * Despite this, it returns a number, not a tree.
 */
public abstract class TreeValueFunction extends Applicable<TreeNode, NumberInterface> {

    @Override
    protected NumberInterface applyInternal(TreeNode[] params) {
        return null;
    }

    @Override
    public NumberInterface apply(TreeNode... params) {
        return null;
    }

    /**
     * Applies the tree value functions to the given tree nodes,
     * using the given reducer.
     * @param reducer the reducer to use.
     * @param params the parameters to apply to.
     * @return the result of the application.
     */
    public abstract NumberInterface applyWithReducerInternal(Reducer<NumberInterface> reducer, TreeNode[] params);

    /**
     * Checks if the given parameters and reducer can be used
     * with this function, and if so, calls the function on them.
     * @param reducer the reducer to use.
     * @param params the parameters to apply to.
     * @return the result of the application, or null if the parameters are incompatible.
     */
    public NumberInterface applyWithReducer(Reducer<NumberInterface> reducer, TreeNode... params) {
        if(!matchesParams(params) || reducer == null) return null;
        return applyWithReducerInternal(reducer, params);
    }
}
